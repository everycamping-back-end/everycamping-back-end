package com.zerobase.everycampingbackend.common.authkey;

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
    public ResponseEntity<?> authCodeRequest(@RequestBody String email){
        authCodeService.authCodeRequest(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth-verify")
    public ResponseEntity<?> authCodeVerify(@RequestBody String email, @RequestBody String code){
        authCodeService.authCodeVerify(email, code);
        return ResponseEntity.ok().build();
    }
}
