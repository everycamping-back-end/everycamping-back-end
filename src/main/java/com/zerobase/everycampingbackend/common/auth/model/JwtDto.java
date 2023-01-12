package com.zerobase.everycampingbackend.common.auth.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
