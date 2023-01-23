package com.zerobase.everycampingbackend.domain.user.dto;

import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String email;
    private String nickName;
    private String address;
    private String zipcode;
    private String phoneNumber;

    public static CustomerDto from(Customer customer){
        return CustomerDto.builder()
            .id(customer.getId())
            .email(customer.getEmail())
            .nickName(customer.getNickName())
            .address(customer.getAddress())
            .zipcode(customer.getZipcode())
            .phoneNumber(customer.getPhone())
            .build();
    }
}
