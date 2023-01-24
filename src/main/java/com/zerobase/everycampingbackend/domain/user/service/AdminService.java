package com.zerobase.everycampingbackend.domain.user.service;

import static com.zerobase.everycampingbackend.domain.auth.issuer.JwtIssuer.REFRESH_EXPIRE_TIME;

import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.domain.auth.service.CustomUserDetailsService;
import com.zerobase.everycampingbackend.domain.auth.type.UserType;
import com.zerobase.everycampingbackend.domain.redis.RedisClient;
import com.zerobase.everycampingbackend.domain.user.entity.Admin;
import com.zerobase.everycampingbackend.domain.user.form.PasswordForm;
import com.zerobase.everycampingbackend.domain.user.form.SignInForm;
import com.zerobase.everycampingbackend.domain.user.form.SignUpForm;
import com.zerobase.everycampingbackend.domain.user.repository.AdminRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements CustomUserDetailsService {

    public static final String RT_REDIS_KEY = "RT-ADMIN";
    private final String SECRET_KEY = "secret1";
    private final AdminRepository adminRepository;
    private final JwtIssuer jwtIssuer;
    private final PasswordEncoder passwordEncoder;
    private final RedisClient redisClient;

    public void signUp(SignUpForm form, String key) {
        if (!SECRET_KEY.equals(key)){
            throw new CustomException(ErrorCode.ACCESS_INVALID);
        }

        if (adminRepository.existsByEmail(form.getEmail().toLowerCase(Locale.ROOT))) {
            throw new CustomException(ErrorCode.EMAIL_BEING_USED);
        }

        adminRepository.save(Admin.from(form, passwordEncoder));
    }

    public JwtDto signIn(SignInForm form) {
        Admin admin = getAdminByEmail(form.getEmail().toLowerCase(Locale.ROOT));

        if (!passwordEncoder.matches(form.getPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }

        return issueJwt(admin.getEmail(), admin.getId());
    }

    public void signOut(String email) {
        deleteRefreshToken(email);
    }

    public void updatePassword(Admin admin, PasswordForm form) {
        if(!passwordEncoder.matches(form.getOldPassword(), admin.getPassword())){
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        admin.setPassword(passwordEncoder.encode(form.getNewPassword()));
        adminRepository.save(admin);
    }

    @Override
    public JwtDto issueJwt(String email, Long id) {
        JwtDto jwtDto = jwtIssuer.createToken(email, id, UserType.ADMIN.name());
        putRefreshToken(email, jwtDto.getRefreshToken());
        return jwtDto;
    }

    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getAdminByEmail(email);
    }

    @Override
    public String getRefreshToken(String email) {
        return redisClient.getValue(RT_REDIS_KEY, email);
    }

    private void putRefreshToken(String email, String token) {
        redisClient.putValue(RT_REDIS_KEY, email, token, REFRESH_EXPIRE_TIME);
    }

    private void deleteRefreshToken(String email) {
        redisClient.deleteValue(RT_REDIS_KEY, email);
    }
}
