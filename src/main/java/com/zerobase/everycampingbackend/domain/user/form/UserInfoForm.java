package com.zerobase.everycampingbackend.domain.user.form;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForm {
    @NotBlank
    private String nickName;
    private String phoneNumber;
    private String address;
    private String zipcode;
}
