package com.zerobase.everycampingbackend.domain.user.dto;

import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto {
    private Long id;
    private String email;
    private String nickName;
    private String address;
    private String zipcode;
    private String phoneNumber;

    public static SellerDto from(Seller seller){
        return SellerDto.builder()
            .id(seller.getId())
            .email(seller.getEmail())
            .nickName(seller.getNickName())
            .address(seller.getAddress())
            .zipcode(seller.getZipcode())
            .phoneNumber(seller.getPhone())
            .build();
    }
}
