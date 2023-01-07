package com.zerobase.everycampingbackend.user.controller;

import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

  private final SellerService sellerService;

  @PostMapping("/signup")
  public ResponseEntity<String> sellerSignUp(@RequestBody SignUpForm form) {
    return ResponseEntity.ok(sellerService.signUp(form));
  }

  @PostMapping("/signin")
  public ResponseEntity<String> sellerSignIn(@RequestBody SignInForm form) {
    return ResponseEntity.ok(sellerService.signIn(form));
  }
}
