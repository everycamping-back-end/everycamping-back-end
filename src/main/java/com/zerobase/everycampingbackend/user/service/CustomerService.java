package com.zerobase.everycampingbackend.user.service;

import com.zerobase.everycampingbackend.common.auth.CustomUserDetailsService;
import com.zerobase.everycampingbackend.common.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.model.UserType;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.domain.repository.CustomerRepository;
import java.util.Collections;
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
public class CustomerService implements CustomUserDetailsService {

    private final CustomerRepository customerRepository;
    private final JwtIssuer jwtIssuer;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    public void signUp(SignUpForm form) {
        if (customerRepository.existsByEmail(form.getEmail().toLowerCase(Locale.ROOT))) {
            throw new CustomException(ErrorCode.EMAIL_BEING_USED);
        }
        customerRepository.save(Customer.from(form, passwordEncoder));
    }


    public JwtDto signIn(SignInForm form) {
        Customer customer = getCustomerByEmail(form.getEmail().toLowerCase(Locale.ROOT));

        if (!passwordEncoder.matches(form.getPassword(), customer.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }

        JwtDto jwtDto = jwtIssuer.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);

        putRefreshToken(customer.getEmail(), jwtDto.getRefreshToken());

        return jwtDto;
    }

    public void signOut(String email){
        deleteRefreshToken(email);
    }

    public Optional<Customer> findByIdAndEmail(Long id, String email) {
        return customerRepository.findById(id)
            .stream().filter(customer -> customer.getEmail().equals(email))
            .findFirst();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email)
            .map(e -> new User(e.getEmail(), "",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public String getRefreshToken(String email) {
        return (String) redisTemplate.opsForValue().get("RT-CUSTOMER:" + email);
    }

    public void putRefreshToken(String email, String token) {
        redisTemplate.opsForValue()
            .set("RT-CUSTOMER:" + email, token, JwtIssuer.EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete("RT-CUSTOMER:" + email);
    }

}
