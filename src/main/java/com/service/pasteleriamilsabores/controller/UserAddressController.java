package com.service.pasteleriamilsabores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.AddressDto;
import com.service.pasteleriamilsabores.service.AddressService;

@RestController
@RequestMapping("/api/users/{run}/addresses")
public class UserAddressController {

    private final AddressService addressService;

    public UserAddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressDto>> list(@PathVariable("run") String run) {
        return ResponseEntity.ok(addressService.listByUserRun(run));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDto> add(@PathVariable("run") String run, @RequestBody AddressDto dto) {
        AddressDto created = addressService.addAddress(run, dto);
        return ResponseEntity.status(201).body(created);
    }

    @DeleteMapping(path = "/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("run") String run, @PathVariable("addressId") String addressId) {
        try {
            addressService.deleteAddress(run, addressId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
