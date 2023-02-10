package com.inditex.infraestructure.adapters.outbound.h2.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRICES")
@IdClass(PriceDatabaseIdDTO.class)
public class PriceDatabaseDTO {

    @Id
    private Long productId;

    @Id
    private Long brandId;

    @Id
    private Long priceList;

    private Integer priority;

    private Double price;

    private String currency;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
