package com.inditex.infraestructure.adapters.outbound.h2;

import com.inditex.domain.exception.EntityNotFoundException;
import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import com.inditex.infraestructure.adapters.outbound.h2.model.PriceDatabaseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepository {

    private final JPAPriceRepository jpaPriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Price> findPriceByBrandIdAndProductIdAndApplicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.info("PriceRepositoryImpl.findPriceByBrandIdAndProductIdAndApplicationDate {}, {} and {}", brandId, productId, applicationDate);

        List<PriceDatabaseDTO> listPricesResponse = jpaPriceRepository.
                findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(brandId, productId, applicationDate, applicationDate)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Prices for brandId %s, productId %s, startDateGreaterThanEqual %s and endDateLessThanEqual %s not found",
                        brandId, productId, applicationDate, applicationDate)));

       return listPricesResponse.stream().map(price -> modelMapper.map(price, Price.class)).collect(Collectors.toList());
    }
}
