package com.zerobase.everycampingbackend.domain.settlement.repository;

import com.zerobase.everycampingbackend.domain.settlement.entity.DailySettlement;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySettlementRepository extends JpaRepository<DailySettlement, Long>,
    DailySettlementRepositoryCustom {

    Page<DailySettlement> findAllBySellerIdAndCreatedAtBetween(Long sellerId, Pageable pageable,
        LocalDateTime start, LocalDateTime end);

    Page<DailySettlement> findAllBySellerId(Long sellerId, Pageable pageable);
}
