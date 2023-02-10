package com.inditex.application;

import com.inditex.domain.model.Price;

import java.time.LocalDateTime;

public interface PriceService {

    public Price getPriceByBrandIdAndProductIdAndApplicationDate(
            Long brandId, Long productId, LocalDateTime applicationDate);
}
