package com.inditex.infraestructure.adapters.inbound.rest;

import com.inditex.application.PriceService;
import com.inditex.domain.model.Price;
import com.inditex.infraestructure.adapters.inbound.rest.PriceController;
import com.inditex.infraestructure.adapters.inbound.rest.model.PriceRestResponseDTO;
import com.inditex.infraestructure.adapters.outbound.h2.model.PriceDatabaseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PriceControllerShould {

    @Mock
    private PriceService priceService;

    private PriceController priceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceController = new PriceController(priceService, getModelMapper());
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2020-06-14T10:00", "1,35455,2020-06-14T16:00", "1,35455,2020-06-14T21:00",
            "1,35455,2020-06-15T10:00", "1,35455,2020-06-16T21:00" })
    @DisplayName("return price information when filter by productId, brandId and applicationDate")
    void return_price_when_filterBy_brandId_and_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        // Given
        Price price = new Price(productId, brandId, 2L, 1, 35.45, "EUR", applicationDate.minusHours(1), applicationDate.plusHours(4));
        given(priceService.getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate)).willReturn(price);

        // When
        ResponseEntity<PriceRestResponseDTO> priceResponse = priceController.getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);

        // Then
        then(priceResponse).isNotNull();
        then(priceResponse.getBody().getPriceList()).isEqualTo(2L);
        then(priceResponse.getBody().getPrice()).isEqualTo(35.45);
        then(priceResponse.getBody().getProductId()).isEqualTo(35455L);
        then(priceResponse.getBody().getBrandId()).isEqualTo(1L);
        then(priceResponse.getBody().getCurrency()).isEqualTo("EUR");
        then(priceResponse.getBody().getStartDate()).isEqualTo(applicationDate.minusHours(1));
        then(priceResponse.getBody().getEndDate()).isEqualTo(applicationDate.plusHours(4));

        BDDMockito.then(priceService).should().getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
    }

    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }
}