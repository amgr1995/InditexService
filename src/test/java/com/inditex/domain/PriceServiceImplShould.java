package com.inditex.domain;

import com.inditex.domain.exception.EntityNotFoundException;
import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PriceServiceImplShould {

    @Mock
    private PriceRepository priceRepository;

    private PriceServiceImpl priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceService = new PriceServiceImpl(priceRepository);
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2020-06-14T10:00", "1,35455,2020-06-14T16:00", "1,35455,2020-06-14T21:00",
            "1,35455,2020-06-15T10:00", "1,35455,2020-06-16T21:00" })
    @DisplayName("return a price information when filter by productId, brandId and applicationDate")
    void return_price_when_filterBy_brandId_and_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        // Given
        Price priceOne = new Price(35455L, 1L, 1L, 0, 25.45, "EUR", applicationDate.minusHours(2), applicationDate.plusHours(1));
        Price priceTwo = new Price(35455L, 1L, 2L, 1, 35.45, "EUR", applicationDate.minusHours(1), applicationDate.plusHours(4));
        List<Price> priceList = List.of(priceOne, priceTwo);

        given(priceRepository.findPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate)).willReturn(priceList);

        // When
        Price priceResponse = priceService.getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);

        // Then
        then(priceResponse).isNotNull();
        then(priceResponse.getPriceList()).isEqualTo(2L);
        then(priceResponse.getPrice()).isEqualTo(35.45);
        then(priceResponse.getProductId()).isEqualTo(35455L);
        then(priceResponse.getBrandId()).isEqualTo(1L);
        then(priceResponse.getPriority()).isEqualTo(1);
        then(priceResponse.getCurrency()).isEqualTo("EUR");
        then(priceResponse.getStartDate()).isEqualTo(applicationDate.minusHours(1));
        then(priceResponse.getEndDate()).isEqualTo(applicationDate.plusHours(4));

        BDDMockito.then(priceRepository).should().findPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2023-06-14T10:00" })
    @DisplayName("return price not found exception when filter by productId, brandId and applicationDate")
    void return_EntityNotFoundException_when_filterBy_brandId_and_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        // Given When Then
        assertThatThrownBy(() ->  priceService.getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Price not found");
    }
}