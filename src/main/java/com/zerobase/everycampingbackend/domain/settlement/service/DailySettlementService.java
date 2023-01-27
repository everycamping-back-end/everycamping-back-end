package com.zerobase.everycampingbackend.domain.settlement.service;

import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDto;
import com.zerobase.everycampingbackend.domain.settlement.form.GetDailySettlementsForm;
import com.zerobase.everycampingbackend.domain.settlement.repository.DailySettlementRepository;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailySettlementService {

    private final DailySettlementRepository dailySettlementRepository;

    public Page<DailySettlementDto> getSettlements(GetDailySettlementsForm form, Seller seller,
        Pageable pageable) {

        LocalDateTime start = form.getStartDate() == null ? null
            : form.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime end = form.getEndDate() == null ? null
            : form.getEndDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime().with(LocalTime.MAX);

        if (start == null || end == null) {
            return dailySettlementRepository.findAllBySellerId(
                seller.getId(), pageable).map(DailySettlementDto::from);
        }

        return dailySettlementRepository.findAllBySellerIdAndCreatedAtBetween(
            seller.getId(), pageable, start, end).map(DailySettlementDto::from);
    }

}
