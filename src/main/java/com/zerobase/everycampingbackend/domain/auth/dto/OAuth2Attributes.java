package com.zerobase.everycampingbackend.domain.auth.dto;

import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {

    private final String name;
    private final String email;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String name,
        String email) {
        this.name = name;
        this.email = email;
    }

    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        }

        throw new CustomException(ErrorCode.WRONG_OAUTH2_PROVIDER);
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .build();
    }

}
