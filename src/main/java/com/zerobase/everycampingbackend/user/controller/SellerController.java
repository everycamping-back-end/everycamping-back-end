package com.zerobase.everycampingbackend.user.controller;

import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
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
  public ResponseEntity sellerSignUp(@RequestBody SignUpForm form) {
    sellerService.signUp(form);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtDto> sellerSignIn(@RequestBody SignInForm form) {
    return ResponseEntity.ok(sellerService.signIn(form));
  }
}
