package com.zerobase.everycampingbackend.domain.settlement.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class DailySettlementDetailDto {

    Long productId; //주문상품 id
    String productNameSnapshot; //주문시 상품명
    Integer quantity; //주문개수
    Integer amount; //주문금액

    Long customerId; // 고객 id
    String customerEmail; //고객 이메일
    String customerNickName; //고객 닉네임

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime orderedAt; //주문일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime confirmedAt; //주문확정일자

}
