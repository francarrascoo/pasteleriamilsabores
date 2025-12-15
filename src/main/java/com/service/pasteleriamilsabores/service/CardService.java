package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.CardDto;
import com.service.pasteleriamilsabores.models.Card;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.repository.CardRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public List<CardDto> listByUserRun(String userRun) {
        ensureUserExists(userRun);
        return cardRepository.findByUserRun(userRun).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardDto addCard(String userRun, CardDto dto) {
        User user = userRepository.findById(userRun)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userRun));
        
        // If this card is marked as default, unset other defaults for this user
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            cardRepository.findByUserRun(userRun).forEach(c -> {
                c.setIsDefault(false);
                cardRepository.save(c);
            });
        }
        
        Card card = new Card();
        card.setId(UUID.randomUUID().toString());
        card.setCardNumber(dto.getCardNumber());
        card.setMonth(dto.getMonth());
        card.setYear(dto.getYear());
        card.setCardholderName(dto.getCardholderName());
        card.setIsDefault(dto.getIsDefault());
        card.setUser(user);
        Card saved = cardRepository.save(card);
        return toDto(saved);
    }

    @Transactional
    public CardDto updateCard(String userRun, String cardId, CardDto dto) {
        ensureUserExists(userRun);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada: " + cardId));
        if (!card.getUser().getRun().equals(userRun)) {
            throw new IllegalArgumentException("La tarjeta no pertenece al usuario");
        }
        
        // If marking this card as default, unset other defaults for this user
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            cardRepository.findByUserRun(userRun).forEach(c -> {
                if (!c.getId().equals(cardId)) {
                    c.setIsDefault(false);
                    cardRepository.save(c);
                }
            });
        }
        
        // Update fields
        if (dto.getCardNumber() != null) card.setCardNumber(dto.getCardNumber());
        if (dto.getMonth() != null) card.setMonth(dto.getMonth());
        if (dto.getYear() != null) card.setYear(dto.getYear());
        if (dto.getCardholderName() != null) card.setCardholderName(dto.getCardholderName());
        if (dto.getIsDefault() != null) card.setIsDefault(dto.getIsDefault());
        
        Card saved = cardRepository.save(card);
        return toDto(saved);
    }

    @Transactional
    public void deleteCard(String userRun, String cardId) {
        ensureUserExists(userRun);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada: " + cardId));
        if (!card.getUser().getRun().equals(userRun)) {
            throw new IllegalArgumentException("La tarjeta no pertenece al usuario");
        }
        cardRepository.deleteById(cardId);
    }

    private void ensureUserExists(String run) {
        if (!userRepository.existsById(run)) {
            throw new EntityNotFoundException("Usuario no encontrado: " + run);
        }
    }

    private CardDto toDto(Card entity) {
        CardDto dto = new CardDto();
        dto.setId(entity.getId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setMonth(entity.getMonth());
        dto.setYear(entity.getYear());
        dto.setCardholderName(entity.getCardholderName());
        dto.setLastFourDigits(extractLastFourDigits(entity.getCardNumber()));
        dto.setIsDefault(entity.getIsDefault());
        return dto;
    }

    private String extractLastFourDigits(String cardNumber) {
        if (cardNumber == null) {
            return null;
        }
        String numeric = cardNumber.replaceAll("[^0-9]", "");
        if (numeric.length() <= 4) {
            return numeric;
        }
        return numeric.substring(numeric.length() - 4);
    }

}

