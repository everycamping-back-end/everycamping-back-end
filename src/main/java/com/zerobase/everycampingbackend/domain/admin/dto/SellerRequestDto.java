package com.zerobase.everycampingbackend.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.admin.entity.SellerRequest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRequestDto {
    private Long id;
    private String email;
    private String nickName;
    private String phone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime registeredAt; // 판매자 가입일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 신청일

    public static SellerRequestDto from(SellerRequest request){
        return SellerRequestDto.builder()
            .id(request.getId())
            .email(request.getSeller().getEmail())
            .nickName(request.getSeller().getNickName())
            .phone(request.getSeller().getPhone())
            .registeredAt(request.getSeller().getCreatedAt())
            .createdAt(request.getCreatedAt())
            .build();
    }
}
