package com.service.pasteleriamilsabores.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.OrderItemRequest;
import com.service.pasteleriamilsabores.dto.OrderItemSummaryDto;
import com.service.pasteleriamilsabores.dto.OrderRequest;
import com.service.pasteleriamilsabores.dto.OrderResponse;
import com.service.pasteleriamilsabores.dto.OrderSummaryDto;
import com.service.pasteleriamilsabores.models.Card;
import com.service.pasteleriamilsabores.models.Pedido;
import com.service.pasteleriamilsabores.models.PedidoItem;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.repository.CardRepository;
import com.service.pasteleriamilsabores.repository.PedidoRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

@Service
public class PedidosService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private VentaDiariaService ventaDiariaService;

    @Transactional
    public OrderResponse createOrder(OrderRequest req) {
        if (req == null) throw new IllegalArgumentException("OrderRequest required");
        User user = userRepository.findById(req.getUserRun()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        String pedidoId = UUID.randomUUID().toString();
        pedido.setId(pedidoId);
        pedido.setUser(user);
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setStatus("CREADO");
        pedido.setDeliveryAddress(req.getDeliveryAddress());

        BigDecimal subtotal = BigDecimal.ZERO;
        List<Object> itemsEcho = new ArrayList<>();

        // build items
        List<PedidoItem> items = new ArrayList<>();
        for (OrderItemRequest ir : req.getItems()) {
            Producto prod = productoRepository.findById(ir.getProductoCodigo()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + ir.getProductoCodigo()));
            BigDecimal unit = prod.getPrecioProducto() == null ? BigDecimal.ZERO : BigDecimal.valueOf(prod.getPrecioProducto());
            Integer qtyInteger = ir.getCantidad();
            int qty = qtyInteger == null ? 0 : qtyInteger;
            // validate stock
            Integer stock = prod.getStock();
            if (stock == null) stock = 0;
            if (qty > stock) {
                throw new IllegalArgumentException("Stock insuficiente para producto: " + prod.getCodigoProducto());
            }
            // reserve/decrement stock
            prod.setStock(stock - qty);
            productoRepository.save(prod);
            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(qty));

            PedidoItem pi = new PedidoItem();
            pi.setId(UUID.randomUUID().toString());
            pi.setProducto(prod);
            pi.setCantidad(qty);
            pi.setPrecioUnitario(unit);
            pi.setTotalPrice(lineTotal);
            pi.setPedido(pedido);
            items.add(pi);

            subtotal = subtotal.add(lineTotal);

            // simple echo
            itemsEcho.add(java.util.Map.of("codigo", prod.getCodigoProducto(), "nombre", prod.getNombreProducto(), "cantidad", qty, "unit", unit, "lineTotal", lineTotal));
        }

        pedido.setItems(items);

        // subtotal before any benefits is kept in `subtotal` if needed later

        // separate cake info was computed previously but not used; removed to simplify

        // 1) apply free cake only if request asks to consume the coupon and user is eligible
        BigDecimal freeCakeAmount = BigDecimal.ZERO;
        boolean freeCakeApplied = false;
        boolean consumeFreeCake = Boolean.TRUE.equals(req.getApplyFreeCakeCoupon());
        boolean isBirthdayToday = false;
        try {
            if (user.getFechaNacimiento() != null && !user.getFechaNacimiento().isBlank()) {
                java.time.LocalDate dob = parseBirthday(user.getFechaNacimiento());
                if (dob != null) {
                    java.time.LocalDate today = java.time.LocalDate.now();
                    isBirthdayToday = (dob.getMonthValue() == today.getMonthValue() && dob.getDayOfMonth() == today.getDayOfMonth());
                }
            }
        } catch (Exception ignored) {
            isBirthdayToday = false;
        }
        boolean alreadyUsedToday = pedidoRepository.findByUserRunOrderByCreatedAtDesc(user.getRun())
                .stream()
                .anyMatch(p -> Boolean.TRUE.equals(p.getFreeCakeApplied()) && p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().equals(java.time.LocalDate.now()));

        // resolve card used for payment (optional)
        Card usedCard = null;
        if (req.getCardId() != null && !req.getCardId().isBlank()) {
            Card c = cardRepository.findById(req.getCardId())
                    .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada: " + req.getCardId()));
            if (!c.getUser().getRun().equals(user.getRun())) {
                throw new IllegalArgumentException("La tarjeta no pertenece al usuario");
            }
            usedCard = c;
            pedido.setCard(c);
        }

        if (consumeFreeCake && Boolean.TRUE.equals(user.getFreeCakeEligible()) && isBirthdayToday && !alreadyUsedToday) {
            for (PedidoItem pi : items) {
                String name = pi.getProducto() == null ? "" : pi.getProducto().getNombreProducto();
                if (name != null && name.toLowerCase().contains("torta") && pi.getCantidad() != null && pi.getCantidad() > 0 && !freeCakeApplied) {
                    BigDecimal unitPrice = pi.getPrecioUnitario() == null ? BigDecimal.ZERO : pi.getPrecioUnitario();
                    freeCakeAmount = freeCakeAmount.add(unitPrice);
                    // reduce line total by unitPrice
                    pi.setTotalPrice(pi.getTotalPrice().subtract(unitPrice));
                    // removed unused cakeSubtotal adjustment
                    freeCakeApplied = true;
                    break;
                }
            }
        }

        // 2) apply discounts; current business rule: apply to ALL items (not only tortas)
        Integer ageDiscountInteger = user.getDiscountPercent();
        Integer lifetimeInteger = user.getLifetimeDiscountPercent();
        int ageDiscount = ageDiscountInteger == null ? 0 : ageDiscountInteger;
        int lifetime = lifetimeInteger == null ? 0 : lifetimeInteger;

        BigDecimal discountAmount = BigDecimal.ZERO;
        boolean applyDiscounts = Boolean.TRUE.equals(req.getApplyDiscounts());
        // base subtotal after any free cake adjustment: recompute from items
        BigDecimal itemsSubtotalBeforeDiscounts = BigDecimal.ZERO;
        for (PedidoItem pi : items) {
            itemsSubtotalBeforeDiscounts = itemsSubtotalBeforeDiscounts.add(pi.getTotalPrice());
        }

        BigDecimal discountedItemsSubtotal = itemsSubtotalBeforeDiscounts;
        if (applyDiscounts && discountedItemsSubtotal.compareTo(BigDecimal.ZERO) > 0) {
            // aplica primero el mayor (lifetime), luego el menor (age)
            if (lifetime > 0) {
                BigDecimal lifetimeDiscount = discountedItemsSubtotal
                        .multiply(BigDecimal.valueOf(lifetime))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                discountAmount = discountAmount.add(lifetimeDiscount);
                discountedItemsSubtotal = discountedItemsSubtotal.subtract(lifetimeDiscount);
            }
            if (ageDiscount > 0) {
                BigDecimal ageDiscountAmount = discountedItemsSubtotal
                        .multiply(BigDecimal.valueOf(ageDiscount))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                discountAmount = discountAmount.add(ageDiscountAmount);
                discountedItemsSubtotal = discountedItemsSubtotal.subtract(ageDiscountAmount);
            }
        }

        // envío fijo 5000 (no afecta descuentos), se suma al final
        BigDecimal shippingCost = BigDecimal.valueOf(5000);
            BigDecimal itemsSubtotalAfterDiscounts = discountedItemsSubtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = itemsSubtotalAfterDiscounts.add(shippingCost).setScale(2, RoundingMode.HALF_UP);

        // persist snapshot fields
        pedido.setFreeCakeApplied(freeCakeApplied);
        pedido.setFreeCakeAmount(freeCakeAmount.setScale(2, RoundingMode.HALF_UP));
        pedido.setDiscountAppliedPercent(ageDiscount > 0 ? ageDiscount : null);
        pedido.setLifetimeDiscountAppliedPercent(lifetime > 0 ? lifetime : null);
        pedido.setTotalAmount(total);
        // Subtotal en DB: solo productos, después de aplicar torta gratis y descuentos, sin envío
        pedido.setSubtotalAmount(itemsSubtotalAfterDiscounts);
        pedido.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        pedido.setShippingCost(shippingCost.setScale(2, RoundingMode.HALF_UP));

        // save pedido
        pedidoRepository.save(pedido);

        // auto-register daily sales for each item
        // We need to distribute the final subtotal (after all discounts) proportionally across items
        // based on each item's contribution to the pre-discount subtotal
        for (PedidoItem pi : items) {
            if (pi.getProducto() != null && pi.getCantidad() != null && pi.getCantidad() > 0) {
                BigDecimal itemTotalBeforeDiscounts = pi.getTotalPrice() == null ? BigDecimal.ZERO : pi.getTotalPrice();
                
                // Calculate what percentage this item represents of the total before discounts
                BigDecimal itemProportion = BigDecimal.ZERO;
                if (itemsSubtotalBeforeDiscounts.compareTo(BigDecimal.ZERO) > 0) {
                    itemProportion = itemTotalBeforeDiscounts.divide(itemsSubtotalBeforeDiscounts, 6, RoundingMode.HALF_UP);
                }
                
                // Apply that proportion to the final discounted subtotal
                BigDecimal ingresosReales = itemsSubtotalAfterDiscounts.multiply(itemProportion).setScale(2, RoundingMode.HALF_UP);
                
                ventaDiariaService.registrarVenta(
                    java.time.LocalDate.now(),
                    pi.getProducto().getCodigoProducto(),
                    pi.getCantidad(),
                    ingresosReales
                );
            }
        }

        OrderResponse resp = new OrderResponse();
        resp.setPedidoId(pedidoId);
        // Subtotal en respuesta: solo productos después de beneficios, sin envío
        resp.setSubtotal(itemsSubtotalAfterDiscounts);
        resp.setShippingCost(shippingCost.setScale(2, RoundingMode.HALF_UP));
        resp.setFreeCakeAmount(freeCakeAmount.setScale(2, RoundingMode.HALF_UP));
        resp.setDiscountPercentApplied(ageDiscount > 0 ? ageDiscount : null);
        resp.setLifetimeDiscountPercentApplied(lifetime > 0 ? lifetime : null);
        resp.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        if (usedCard != null) {
            resp.setCardId(usedCard.getId());
            resp.setCardLastFour(extractLastFour(usedCard.getCardNumber()));
        }
        resp.setTotal(total);
        resp.setItems(itemsEcho);

        // suggest available tortas (products with 'torta' in name and stock>0)
        List<String> cakeCandidates = new ArrayList<>();
        for (Producto p : productoRepository.findAll()) {
            String name = p.getNombreProducto() == null ? "" : p.getNombreProducto();
            Integer s = p.getStock();
            if (name.toLowerCase().contains("torta") && s != null && s > 0) {
                cakeCandidates.add(p.getCodigoProducto());
            }
        }
        boolean freeCakeAvailable = Boolean.TRUE.equals(user.getFreeCakeEligible()) && !cakeCandidates.isEmpty();
        resp.setFreeCakeAvailable(freeCakeAvailable);
        resp.setFreeCakeSuggestedProducts(cakeCandidates);

        return resp;
    }

    public List<OrderSummaryDto> listOrdersForUser(String run) {
        if (run == null || run.isBlank()) {
            return List.of();
        }
        return pedidoRepository.findByUserRunOrderByCreatedAtDesc(run)
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    public List<OrderSummaryDto> listAllOrders() {
        return pedidoRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    private OrderSummaryDto toSummaryDto(Pedido pedido) {
        OrderSummaryDto dto = new OrderSummaryDto();
        dto.setPedidoId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setCreatedAt(pedido.getCreatedAt());
        dto.setTotal(pedido.getTotalAmount());
        dto.setShippingCost(pedido.getShippingCost());
        dto.setDeliveryAddress(pedido.getDeliveryAddress());
        dto.setFreeCakeApplied(pedido.getFreeCakeApplied());
        dto.setFreeCakeAmount(pedido.getFreeCakeAmount());
        dto.setDiscountAppliedPercent(pedido.getDiscountAppliedPercent());
        dto.setLifetimeDiscountAppliedPercent(pedido.getLifetimeDiscountAppliedPercent());
        dto.setSubtotal(pedido.getSubtotalAmount());
        dto.setDiscountAmount(pedido.getDiscountAmount());
        dto.setTotalSinDescuento(pedido.getSubtotalAmount());
        dto.setTotalConDescuento(pedido.getTotalAmount());
        if (pedido.getCard() != null) {
            dto.setCardId(pedido.getCard().getId());
            dto.setCardLastFour(extractLastFour(pedido.getCard().getCardNumber()));
        }
        if (pedido.getUser() != null) {
            dto.setPurchaserRun(pedido.getUser().getRun());
            dto.setPurchaserNombre(pedido.getUser().getNombre());
            dto.setPurchaserApellidos(pedido.getUser().getApellidos());
            dto.setPurchaserCorreo(pedido.getUser().getCorreo());
            dto.setPurchaserTelefono(pedido.getUser().getTelefono());
        }
        List<OrderItemSummaryDto> items = pedido.getItems().stream().map(pi -> {
            OrderItemSummaryDto item = new OrderItemSummaryDto();
            if (pi.getProducto() != null) {
                item.setProductoCodigo(pi.getProducto().getCodigoProducto());
                item.setProductoNombre(pi.getProducto().getNombreProducto());
            }
            item.setCantidad(pi.getCantidad());
            item.setPrecioUnitario(pi.getPrecioUnitario());
            item.setTotalPrice(pi.getTotalPrice());
            return item;
        }).toList();
        dto.setItems(items);
        return dto;
    }

    /**
     * Parse birthday supporting common formats: ISO (YYYY-MM-DD) and dd/MM/yyyy.
     */
    private java.time.LocalDate parseBirthday(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        // Try ISO first
        try {
            return java.time.LocalDate.parse(s);
        } catch (Exception ignored) { }
        // Try dd/MM/yyyy
        try {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return java.time.LocalDate.parse(s, fmt);
        } catch (Exception ignored) { }
        // Try d/M/yyyy
        try {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy");
            return java.time.LocalDate.parse(s, fmt);
        } catch (Exception ignored) { }
        return null;
    }

    private String extractLastFour(String cardNumber) {
        if (cardNumber == null) return null;
        String numeric = cardNumber.replaceAll("[^0-9]", "");
        if (numeric.length() <= 4) return numeric;
        return numeric.substring(numeric.length() - 4);
    }
}
