package com.inditex.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Serializable {

    private Long productId;

    private Long brandId;

    private Long priceList;

    private Integer priority;

    private Double price;

    private String currency;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
