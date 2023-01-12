package com.zerobase.everycampingbackend.user.service;

import com.zerobase.everycampingbackend.common.auth.CustomUserDetailsService;
import com.zerobase.everycampingbackend.common.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.model.UserType;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.domain.repository.SellerRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService implements CustomUserDetailsService {

    private final SellerRepository sellerRepository;
    private final JwtIssuer jwtIssuer;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    public void signUp(SignUpForm form) {
        if(sellerRepository.existsByEmail(form.getEmail().toLowerCase(Locale.ROOT))){
            throw new CustomException(ErrorCode.EMAIL_BEING_USED);
        }
        sellerRepository.save(Seller.from(form, passwordEncoder));
    }

    public JwtDto signIn(SignInForm form) {
        Seller seller = getSellerByEmail(form.getEmail().toLowerCase(Locale.ROOT));

        if(!passwordEncoder.matches(form.getPassword(), seller.getPassword())){
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }

        JwtDto jwtDto = jwtIssuer.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);

        putRefreshToken(seller.getEmail(), jwtDto.getRefreshToken());

        return jwtDto;
    }

    public void signOut(String email){
        deleteRefreshToken(email);
    }

    public Optional<Seller> findByIdAndEmail(Long id, String email) {
        return sellerRepository.findById(id)
            .stream().filter(seller -> seller.getEmail().equals(email))
            .findFirst();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Seller seller = getSellerByEmail(email);
        return new User(seller.getEmail(), "",
                List.of(new SimpleGrantedAuthority("ROLE_SELLER")));
    }

    @Override
    public String getRefreshToken(String email) {
        return (String) redisTemplate.opsForValue().get("RT-SELLER:" + email);
    }

    public void putRefreshToken(String email, String token) {
        redisTemplate.opsForValue()
            .set("RT-SELLER:" + email, token, JwtIssuer.EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete("RT-SELLER:" + email);
    }
}
