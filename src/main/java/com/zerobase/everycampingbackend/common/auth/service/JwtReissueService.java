package com.zerobase.everycampingbackend.common.auth.service;

import com.zerobase.everycampingbackend.common.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.model.UserVo;
import com.zerobase.everycampingbackend.common.auth.provider.JwtAuthenticationProvider;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtReissueService {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtIssuer jwtIssuer;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    public JwtDto reissue(JwtDto tokens) {
        if (!jwtAuthenticationProvider.validateToken(tokens)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        Claims claims = jwtIssuer.getClaims(tokens.getAccessToken());
        UserVo userVo = jwtIssuer.getUserVo(claims);
        String role = jwtAuthenticationProvider.getRoleFromClaims(claims);

        if (!tokens.getRefreshToken()
            .equals(customUserDetailsService.getRefreshToken(role, userVo.getEmail()))) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }

        return customUserDetailsService.issueJwt(role, userVo);
    }
}
