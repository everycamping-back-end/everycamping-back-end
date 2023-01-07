package com.zerobase.everycampingbackend.user.controller;


import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.domain.repository.CustomerRepository;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor

public class CustomerController {

  private final CustomerService customerService;


  @PostMapping("/signup")
  public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form) {
    return ResponseEntity.ok(customerService.signUp(form));
  }

  @PostMapping("/signin")
  public ResponseEntity<String> customerSignIn(@RequestBody SignInForm form) {
    return ResponseEntity.ok(customerService.signIn(form));
  }
}
