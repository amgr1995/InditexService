package com.inditex.infraestructure.adapters.outbound.h2;

import com.inditex.infraestructure.adapters.outbound.h2.model.PriceDatabaseDTO;
import com.inditex.infraestructure.adapters.outbound.h2.model.PriceDatabaseIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JPAPriceRepository extends JpaRepository<PriceDatabaseDTO, PriceDatabaseIdDTO> {

    public Optional<List<PriceDatabaseDTO>> findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long brandId, Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
