package com.inditex.infraestructure.adapters.outbound.h2;

import com.inditex.domain.PriceServiceImpl;
import com.inditex.domain.exception.EntityNotFoundException;
import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import com.inditex.infraestructure.adapters.outbound.h2.JPAPriceRepository;
import com.inditex.infraestructure.adapters.outbound.h2.PriceRepositoryImpl;
import com.inditex.infraestructure.adapters.outbound.h2.model.PriceDatabaseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PriceRepositoryImplShould {

    @Mock
    private JPAPriceRepository jpaPriceRepository;
    private PriceRepositoryImpl priceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceRepository = new PriceRepositoryImpl(jpaPriceRepository, getModelMapper());
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2020-06-14T10:00", "1,35455,2020-06-14T16:00", "1,35455,2020-06-14T21:00",
            "1,35455,2020-06-15T10:00", "1,35455,2020-06-16T21:00" })
    @DisplayName("return price list information when filter by productId, brandId and applicationDate")
    void return_price_list_when_filterBy_brandId_and_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        // Given
        PriceDatabaseDTO priceOne = new PriceDatabaseDTO(35455L, 1L, 1L, 0, 25.45, "EUR", applicationDate.minusHours(2), applicationDate.plusHours(1));
        PriceDatabaseDTO priceTwo = new PriceDatabaseDTO(35455L, 1L, 2L, 1, 35.45, "EUR", applicationDate.minusHours(1), applicationDate.plusHours(4));
        List<PriceDatabaseDTO> priceList = List.of(priceOne, priceTwo);

        given(jpaPriceRepository.findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(brandId, productId, applicationDate, applicationDate)).willReturn(Optional.of(priceList));

        // When
        List<Price> priceResponse = priceRepository.findPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);

        // Then
        then(priceResponse).isNotNull();
        then(priceResponse.size()).isEqualTo(2);

        then(priceResponse.get(0).getPriceList()).isEqualTo(1L);
        then(priceResponse.get(0).getPrice()).isEqualTo(25.45);
        then(priceResponse.get(0).getProductId()).isEqualTo(35455L);
        then(priceResponse.get(0).getBrandId()).isEqualTo(1L);
        then(priceResponse.get(0).getPriority()).isEqualTo(0);
        then(priceResponse.get(0).getCurrency()).isEqualTo("EUR");
        then(priceResponse.get(0).getStartDate()).isEqualTo(applicationDate.minusHours(2));
        then(priceResponse.get(0).getEndDate()).isEqualTo(applicationDate.plusHours(1));

        then(priceResponse.get(1).getPriceList()).isEqualTo(2L);
        then(priceResponse.get(1).getPrice()).isEqualTo(35.45);
        then(priceResponse.get(1).getProductId()).isEqualTo(35455L);
        then(priceResponse.get(1).getBrandId()).isEqualTo(1L);
        then(priceResponse.get(1).getPriority()).isEqualTo(1);
        then(priceResponse.get(1).getCurrency()).isEqualTo("EUR");
        then(priceResponse.get(1).getStartDate()).isEqualTo(applicationDate.minusHours(1));
        then(priceResponse.get(1).getEndDate()).isEqualTo(applicationDate.plusHours(4));

        BDDMockito.then(jpaPriceRepository).should().findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(brandId, productId, applicationDate, applicationDate);
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2023-06-14T10:00" })
    @DisplayName("return price not found exception when filter by productId, brandId and applicationDate")
    void return_EntityNotFoundException_when_filterBy_brandId_and_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        // Given When Then
        assertThatThrownBy(() ->  priceRepository.findPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(
                        "Prices for brandId %s, productId %s, startDateGreaterThanEqual %s and endDateLessThanEqual %s not found",
                        brandId, productId, applicationDate, applicationDate));
    }

    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }
}