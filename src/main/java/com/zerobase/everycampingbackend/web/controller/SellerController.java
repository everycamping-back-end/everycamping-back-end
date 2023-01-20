package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.auth.model.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.service.JwtReissueService;
import com.zerobase.everycampingbackend.domain.user.dto.SellerDto;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import com.zerobase.everycampingbackend.domain.user.form.PasswordForm;
import com.zerobase.everycampingbackend.domain.user.form.SignInForm;
import com.zerobase.everycampingbackend.domain.user.form.SignUpForm;
import com.zerobase.everycampingbackend.domain.user.form.UserInfoForm;
import com.zerobase.everycampingbackend.domain.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final JwtReissueService jwtReissueService;

    @PostMapping("/signup")
    public ResponseEntity<?> sellerSignUp(@RequestBody SignUpForm form) {
        sellerService.signUp(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> sellerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(sellerService.signIn(form));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> sellerSignOut(@AuthenticationPrincipal Seller seller) {
        sellerService.signOut(seller.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> jwtReissue(@RequestBody JwtDto jwtDto){
        return ResponseEntity.ok(jwtReissueService.reissue(jwtDto));
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@AuthenticationPrincipal Seller seller,
        @RequestBody UserInfoForm form) {
        sellerService.updateInfo(seller, form);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<SellerDto> getInfo(@AuthenticationPrincipal Seller seller){
        return ResponseEntity.ok(sellerService.getInfo(seller));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal Seller seller,
        @RequestBody PasswordForm form){
        sellerService.updatePassword(seller, form);
        return ResponseEntity.ok().build();
    }
}
