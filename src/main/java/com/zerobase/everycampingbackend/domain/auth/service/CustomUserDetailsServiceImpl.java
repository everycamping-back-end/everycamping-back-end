package com.zerobase.everycampingbackend.domain.auth.service;

import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.type.UserType;
import com.zerobase.everycampingbackend.domain.auth.dto.UserVo;
import com.zerobase.everycampingbackend.domain.user.service.CustomerService;
import com.zerobase.everycampingbackend.domain.user.service.SellerService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final Map<String, CustomUserDetailsService> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(UserType.CUSTOMER.name(), customerService);
        map.put(UserType.SELLER.name(), sellerService);
    }

    public UserDetails loadUserByUsername(String key, String email) throws UsernameNotFoundException{
        return map.get(key).loadUserByUsername(email);
    }

    public String getRefreshToken(String key, String email){
        return map.get(key).getRefreshToken(email);
    }

    public JwtDto issueJwt(String key, UserVo userVo){
        return map.get(key).issueJwt(userVo.getEmail(), userVo.getId());
    }

    @Override
    public String getRefreshToken(String email) {
        return null;
    }

    @Override
    public JwtDto issueJwt(String email, Long id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
