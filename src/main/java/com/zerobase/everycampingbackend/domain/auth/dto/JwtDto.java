package com.zerobase.everycampingbackend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private String accessToken;
    @NotBlank
    @JsonProperty
    private String refreshToken;
}
