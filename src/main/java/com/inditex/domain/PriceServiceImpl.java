package com.inditex.domain;

import com.inditex.application.PriceService;
import com.inditex.domain.exception.EntityNotFoundException;
import com.inditex.domain.exception.PriceExceptionMessages;
import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public Price getPriceByBrandIdAndProductIdAndApplicationDate(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.info("PriceServiceImpl.getPriceByBrandIdAndProductIdAndApplicationDate {}, {} and {}",
                brandId, productId, applicationDate);

        List<Price> priceResponse = priceRepository.findPriceByBrandIdAndProductIdAndApplicationDate
                (brandId, productId, applicationDate);

        return priceResponse.stream().sorted(Comparator.comparing(Price::getPriority).reversed()).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(PriceExceptionMessages.PRICE_NOT_FOUND.getMessage()));
    }
}
