package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDetailDto;
import com.zerobase.everycampingbackend.domain.settlement.dto.DailySettlementDto;
import com.zerobase.everycampingbackend.domain.settlement.form.GetDailySettlementsForm;
import com.zerobase.everycampingbackend.domain.settlement.service.DailySettlementService;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
@Slf4j
public class SettlementController {

    private final DailySettlementService dailySettlementService;

    @GetMapping()
    public ResponseEntity<Page<DailySettlementDto>> getSettlements(
        @AuthenticationPrincipal Seller seller,
        @ModelAttribute GetDailySettlementsForm form,
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(dailySettlementService.getSettlements(form, seller, pageable));
    }

    @GetMapping("/{settlementId}")
    public ResponseEntity<Page<DailySettlementDetailDto>> getSettlementDetail(
        @AuthenticationPrincipal Seller seller,
        @PathVariable @NotNull Long settlementId,
        @PageableDefault(sort = "orderedAt", direction = Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(
            dailySettlementService.getSettlementDetail(settlementId, seller, pageable));
    }
}
