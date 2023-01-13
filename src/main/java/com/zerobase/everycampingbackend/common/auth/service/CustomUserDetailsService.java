package com.zerobase.everycampingbackend.common.auth.service;

import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    String getRefreshToken(String email);
    JwtDto issueJwt(String email, Long id);

}
