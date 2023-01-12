package com.zerobase.everycampingbackend.common.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    String getRefreshToken(String email);

}
