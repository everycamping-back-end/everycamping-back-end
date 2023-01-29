package com.zerobase.everycampingbackend.domain.auth.service;

import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    String getRefreshToken(String email);
    JwtDto issueJwt(String email, Long id);

}
