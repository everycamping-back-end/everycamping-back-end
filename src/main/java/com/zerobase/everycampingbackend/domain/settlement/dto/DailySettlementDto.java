package com.zerobase.everycampingbackend.domain.settlement.dto;


import com.zerobase.everycampingbackend.domain.settlement.entity.DailySettlement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailySettlementDto {

    private Long id;
    private Long amount; //총액
    private LocalDate targetDate; //정산대상일
    private LocalDateTime createdAt; //정산처리일

    public static DailySettlementDto from(DailySettlement dailySettlement) {

        return DailySettlementDto.builder()
            .id(dailySettlement.getId())
            .amount(dailySettlement.getAmount())
            .targetDate(dailySettlement.getTargetDate())
            .createdAt(dailySettlement.getCreatedAt())
            .build();
    }
}
