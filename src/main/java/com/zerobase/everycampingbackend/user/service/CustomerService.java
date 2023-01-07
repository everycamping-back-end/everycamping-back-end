package com.zerobase.everycampingbackend.user.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.common.token.config.JwtAuthenticationProvider;
import com.zerobase.everycampingbackend.common.token.model.UserType;
import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.repository.CustomerRepository;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;

  private final JwtAuthenticationProvider provider;

  private boolean isEmailExist(String email){
      return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
          .isPresent();
  }

  private Optional<Customer> findValidCustomer(String email, String password){
    return customerRepository.findByEmail(email).stream()
        .filter(customer -> customer.getPassword().equals(password))
        .findFirst();
  }

  public String signUp(SignUpForm form){

    Customer customer = customerRepository.save(Customer.from(form));

    return "회원 가입에 성공하였습니다.";
  }


  public String signIn(SignInForm form){
     // 로그인 가능 여부 체크
    Customer c = this.findValidCustomer(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

    return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
  }

  public Optional<Customer> findByIdAndEmail(Long id, String email){
    return customerRepository.findById(id)
        .stream().filter(customer -> customer.getEmail().equals(email))
        .findFirst();
  }
}
