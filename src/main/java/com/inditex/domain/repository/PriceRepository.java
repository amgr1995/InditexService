package com.inditex.domain.repository;

import com.inditex.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    public List<Price> findPriceByBrandIdAndProductIdAndApplicationDate(
            Long brandId, Long productId, LocalDateTime applicationDate);
}
