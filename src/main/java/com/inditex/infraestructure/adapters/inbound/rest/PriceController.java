package com.inditex.infraestructure.adapters.inbound.rest;

import com.inditex.application.PriceService;
import com.inditex.domain.model.Price;
import com.inditex.infraestructure.adapters.inbound.rest.model.PriceRestResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping(path = PriceController.BASE_PATH, produces = MediaType.APPLICATION_JSON_VALUE, headers = "Accept="
        + MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PriceController {

    public static final String BASE_PATH = "/price";

    private final PriceService priceService;
    private final ModelMapper modelMapper;

    @GetMapping("/{brandId}/{productId}")
    @SecurityRequirement(name = "inditex")
    public ResponseEntity<PriceRestResponseDTO> getPriceByBrandIdAndProductIdAndApplicationDate(
            @PathVariable("brandId") Long brandId,
            @PathVariable("productId") Long productId,
            @RequestParam(value = "applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        log.info("PriceController.getPriceByBrandIdAndProductIdAndApplicationDate {}, {} and {}", brandId, productId, applicationDate);

        Price priceResponse = priceService.getPriceByBrandIdAndProductIdAndApplicationDate(
                brandId, productId, applicationDate);
        return ResponseEntity.ok(modelMapper.map(priceResponse, PriceRestResponseDTO.class));
    }
}
