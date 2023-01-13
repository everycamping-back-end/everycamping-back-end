package com.zerobase.everycampingbackend.common.auth.provider;


import com.zerobase.everycampingbackend.common.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.model.UserVo;
import com.zerobase.everycampingbackend.common.auth.service.CustomUserDetailsServiceImpl;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtIssuer jwtIssuer;

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = jwtIssuer.getClaims(token);
        if (claims == null) {
            return false;
        }
        if (claims.getExpiration().before(new Date())) {
            throw new CustomException(ErrorCode.TOKEN_NOT_ALIVE);
        }
        UserVo userVo = jwtIssuer.getUserVo(claims);
        if (ObjectUtils.isEmpty(
            customUserDetailsService.getRefreshToken(getRoleFromClaims(claims), userVo.getEmail()))) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }

        return true;
    }

    public boolean validateToken(JwtDto jwtDto) {
        if (!StringUtils.hasText(jwtDto.getAccessToken())
            || !StringUtils.hasText(jwtDto.getRefreshToken())) {
            return false;
        }

        Claims accessClaims = jwtIssuer.getClaims(jwtDto.getAccessToken());

        if (accessClaims != null && accessClaims.getExpiration().after(new Date())) {
            throw new CustomException(ErrorCode.TOKEN_STILL_ALIVE);
        }

        Claims refreshClaims = jwtIssuer.getClaims(jwtDto.getRefreshToken());

        return accessClaims != null && refreshClaims != null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtIssuer.getClaims(token);
        UserVo userVo = jwtIssuer.getUserVo(claims);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(
            getRoleFromClaims(claims), userVo.getEmail());

        return new UsernamePasswordAuthenticationToken(userDetails, userVo,
            userDetails.getAuthorities());
    }

    public String getRoleFromClaims(Claims claims) {
        return claims.get(JwtIssuer.KEY_ROLES, String.class);
    }

}
