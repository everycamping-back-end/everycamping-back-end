package com.zerobase.everycampingbackend.web.controller;


import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.form.CodeForm;
import com.zerobase.everycampingbackend.domain.auth.service.JwtReissueService;
import com.zerobase.everycampingbackend.domain.auth.service.OAuth2UserService;
import com.zerobase.everycampingbackend.domain.user.dto.CustomerDto;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.domain.user.form.PasswordForm;
import com.zerobase.everycampingbackend.domain.user.form.SignInForm;
import com.zerobase.everycampingbackend.domain.user.form.SignUpForm;
import com.zerobase.everycampingbackend.domain.user.form.SocialSignInForm;
import com.zerobase.everycampingbackend.domain.user.form.UserInfoForm;
import com.zerobase.everycampingbackend.domain.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final OAuth2UserService oAuth2UserService;
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

//    @GetMapping("/signin/social/{provider}")
//    public void customerSocialSignIn(@PathVariable String provider,
//        HttpServletResponse response) throws IOException {
//        response.sendRedirect("/oauth2/authorization/" + provider);
//
//    }

    @PostMapping("/signin/social/{provider}")
    public ResponseEntity<JwtDto> customerSocialSignIn(@PathVariable String provider,
        @RequestBody CodeForm codeForm) {
        SocialSignInForm form = oAuth2UserService.signIn(provider, codeForm.getCode());
        return ResponseEntity.ok(customerService.socialSignIn(form.getEmail(), form.getNickName()));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> customerSignOut(@AuthenticationPrincipal Customer customer) {
        customerService.signOut(customer.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> jwtReissue(@RequestBody JwtDto jwtDto) {
        return ResponseEntity.ok(jwtReissueService.reissue(jwtDto));
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@AuthenticationPrincipal Customer customer,
        @RequestBody UserInfoForm form) {
        customerService.updateInfo(customer, form);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<CustomerDto> getInfo(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(customerService.getInfo(customer));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal Customer customer,
        @RequestBody PasswordForm form) {
        customerService.updatePassword(customer, form);
        return ResponseEntity.ok().build();
    }

}
