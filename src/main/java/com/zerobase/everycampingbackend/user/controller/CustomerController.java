package com.zerobase.everycampingbackend.user.controller;


import com.zerobase.everycampingbackend.common.auth.model.JwtDto;
import com.zerobase.everycampingbackend.common.auth.service.JwtReissueService;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;
    private final JwtReissueService jwtReissueService;

    @PostMapping("/signup")
    public ResponseEntity<?> customerSignUp(@RequestBody SignUpForm form) {
        customerService.signUp(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> customerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(customerService.signIn(form));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> customerSignOut(@AuthenticationPrincipal Customer customer){
        customerService.signOut(customer.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> jwtReissue(@RequestBody JwtDto jwtDto){
        return ResponseEntity.ok(jwtReissueService.reissue(jwtDto));
    }

}
