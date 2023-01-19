package com.zerobase.everycampingbackend.common.authkey;

import com.zerobase.everycampingbackend.domain.mail.MailClient;
import com.zerobase.everycampingbackend.domain.mail.MailForm;
import com.zerobase.everycampingbackend.domain.redis.RedisClient;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCodeService {
    private final RedisClient redisClient;
    private final MailClient mailClient;


    public void authCodeRequest(String email) {
        String code = getRandomCode();
        redisClient.putRefreshToken("AUTHCODE", email, code, 1000 * 60 * 10);

        mailClient.sendMail(generateEmailForm(email, code));
    }

    public void authCodeRequest(MailForm form) {
        String code = getRandomCode();
        redisClient.putRefreshToken("AUTHCODE", form.getTo(), code, 1000 * 60 * 10);

        mailClient.sendMail(form);
    }

    private MailForm generateEmailForm(String email, String code) {
        return MailForm.builder()
            .to(email)
            .subject("Everycamping 인증 메일입니다.")
            .text("인증 코드입니다.\n" + code)
            .build();
    }

    private String getRandomCode() {
        return RandomStringUtils.random(8, true, true);
    }

    public void authCodeVerify(String email, String code) {
        if(!code.equals(redisClient.getRefreshToken("AUTHCODE", email))){
            throw new CustomException(ErrorCode.AUTH_CODE_NOT_VALID);
        }
        redisClient.deleteRefreshToken("AUTHCODE", email);
    }
}
