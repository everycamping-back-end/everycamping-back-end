package com.zerobase.everycampingbackend.domain.user.service;

import static com.zerobase.everycampingbackend.domain.auth.issuer.JwtIssuer.REFRESH_EXPIRE_TIME;

import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.issuer.JwtIssuer;
import com.zerobase.everycampingbackend.domain.auth.service.CustomUserDetailsService;
import com.zerobase.everycampingbackend.domain.auth.type.UserType;
import com.zerobase.everycampingbackend.domain.redis.RedisClient;
import com.zerobase.everycampingbackend.domain.user.dto.CustomerDto;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.domain.user.form.PasswordForm;
import com.zerobase.everycampingbackend.domain.user.form.SignInForm;
import com.zerobase.everycampingbackend.domain.user.form.SignUpForm;
import com.zerobase.everycampingbackend.domain.user.form.UserInfoForm;
import com.zerobase.everycampingbackend.domain.user.repository.CustomerRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomUserDetailsService {

    public static final String RT_REDIS_KEY = "RT-CUSTOMER";
    private final CustomerRepository customerRepository;
    private final JwtIssuer jwtIssuer;
    private final PasswordEncoder passwordEncoder;
    private final RedisClient redisClient;

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

        return issueJwt(customer.getEmail(), customer.getId());
    }

    public JwtDto socialSignIn(String email, String nickname) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        Customer customer;
        customer = optionalCustomer.orElseGet(
            () -> customerRepository.save(Customer.of(email, nickname)));

        return issueJwt(customer.getEmail(), customer.getId());
    }

//    public JwtDto socialSignIn(SocialSignInForm form) {
//        Optional<Customer> optionalCustomer = customerRepository.findByEmail(form.getEmail());
//        Customer customer;
//        customer = optionalCustomer.orElseGet(
//            () -> customerRepository.save(Customer.of(form.getEmail(), form.getNickName())));
//
//        return issueJwt(customer.getEmail(), customer.getId());
//    }

    public void signOut(String email) {
        deleteRefreshToken(email);
    }

    public void updateInfo(Customer customer, UserInfoForm form) {
        customer.setNickName(form.getNickName());
        customer.setPhone(form.getPhoneNumber());
        customer.setAddress(form.getAddress());
        customer.setZipcode(form.getZipcode());
        customerRepository.save(customer);
    }

    public CustomerDto getInfo(Customer customer){
        return CustomerDto.from(customer);
    }

    public void updatePassword(Customer customer, PasswordForm form) {
        if(!passwordEncoder.matches(form.getOldPassword(), customer.getPassword())){
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        customer.setPassword(passwordEncoder.encode(form.getNewPassword()));
        customerRepository.save(customer);
    }

    @Override
    public JwtDto issueJwt(String email, Long id) {
        JwtDto jwtDto = jwtIssuer.createToken(email, id, UserType.CUSTOMER.name());
        putRefreshToken(email, jwtDto.getRefreshToken());
        return jwtDto;
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
        return getCustomerByEmail(email);
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
