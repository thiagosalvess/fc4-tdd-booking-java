package com.thiagosalvess.booking.infrastructure.web.controllers;

import com.thiagosalvess.booking.application.dtos.CreatePropertyDto;
import com.thiagosalvess.booking.application.dtos.PropertyResponse;
import com.thiagosalvess.booking.application.services.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> createProperty(@RequestBody CreatePropertyDto dto) {
        var property = propertyService.createProperty(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Property created successfully",
                        "property", PropertyResponse.from(property)
                ));
    }

}
