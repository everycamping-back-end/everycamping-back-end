package com.zerobase.everycampingbackend.domain.settlement.repository;

import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DailySettlementRepositoryCustom {

    Page<DailySettlementDetailDto> getSettlementDetail(Long dailySettlementId, Pageable pageable);
}
