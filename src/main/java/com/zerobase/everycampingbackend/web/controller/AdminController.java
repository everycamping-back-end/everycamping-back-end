package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.admin.dto.SellerRequestDto;
import com.zerobase.everycampingbackend.domain.admin.service.SellerRequestService;
import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.service.JwtReissueService;
import com.zerobase.everycampingbackend.domain.user.entity.Admin;
import com.zerobase.everycampingbackend.domain.user.form.PasswordForm;
import com.zerobase.everycampingbackend.domain.user.form.SignInForm;
import com.zerobase.everycampingbackend.domain.user.form.SignUpForm;
import com.zerobase.everycampingbackend.domain.user.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private final SellerRequestService sellerRequestService;
    private final AdminService adminService;
    private final JwtReissueService jwtReissueService;

    @GetMapping("/seller")
    public ResponseEntity<Page<SellerRequestDto>> getRequests(Pageable pageable){
        return ResponseEntity.ok(sellerRequestService.getSellerRequests(pageable));
    }

    @PutMapping("/seller")
    public ResponseEntity<?> confirm(@RequestBody List<Long> ids){
        sellerRequestService.confirmSellerRequests(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpForm form, @RequestParam String key) {
        adminService.signUp(form, key);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(adminService.signIn(form));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal Admin admin) {
        adminService.signOut(admin.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> jwtReissue(@RequestBody JwtDto jwtDto) {
        return ResponseEntity.ok(jwtReissueService.reissue(jwtDto));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal Admin admin,
        @RequestBody PasswordForm form) {
        adminService.updatePassword(admin, form);
        return ResponseEntity.ok().build();
    }
}
