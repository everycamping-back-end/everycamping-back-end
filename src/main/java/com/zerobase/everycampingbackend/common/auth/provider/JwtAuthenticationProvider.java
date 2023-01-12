package com.zerobase.everycampingbackend.common.auth.provider;


import com.zerobase.everycampingbackend.common.auth.CustomUserDetailsService;
import com.zerobase.everycampingbackend.common.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.model.UserType;
import com.zerobase.everycampingbackend.common.auth.model.UserVo;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import com.zerobase.everycampingbackend.user.service.SellerService;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
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

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtIssuer jwtIssuer;
    private final Map<String, CustomUserDetailsService> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(UserType.CUSTOMER.name(), customerService);
        map.put(UserType.SELLER.name(), sellerService);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = jwtIssuer.getClaims(token);
        if (claims == null) {
            return false;
        }
        if (claims.getExpiration().before(new Date())){
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        UserVo userVo = jwtIssuer.getUserVo(claims);
        if(ObjectUtils.isEmpty(
                getUserDetailsService(claims).getRefreshToken(userVo.getEmail()))) {
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
        Claims refreshClaims = jwtIssuer.getClaims(jwtDto.getRefreshToken());

        return accessClaims != null && refreshClaims != null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtIssuer.getClaims(token);
        UserVo userVo = jwtIssuer.getUserVo(claims);
        UserDetails userDetails = getUserDetailsService(claims).loadUserByUsername(userVo.getEmail());

        return new UsernamePasswordAuthenticationToken(userDetails, userVo,
            userDetails.getAuthorities());
    }

    private CustomUserDetailsService getUserDetailsService(Claims claims) {
        return map.get(claims.get(JwtIssuer.KEY_ROLES, String.class));
    }

}
