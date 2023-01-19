package com.zerobase.everycampingbackend.common.authcode;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeForm {
    @NotBlank
    private String email;
    private String code;
}
