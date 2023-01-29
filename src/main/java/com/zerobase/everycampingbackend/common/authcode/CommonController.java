package com.zerobase.everycampingbackend.common.authcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commons")
public class CommonController {
    private final AuthCodeService authCodeService;

    @PostMapping("/auth-request")
    public ResponseEntity<?> authCodeRequest(@RequestBody AuthCodeForm form){
        authCodeService.authCodeRequest(form.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth-verify")
    public ResponseEntity<?> authCodeVerify(@RequestBody AuthCodeForm form){
        authCodeService.authCodeVerify(form.getEmail(), form.getCode());
        return ResponseEntity.ok().build();
    }
}
