package com.zerobase.everycampingbackend.domain.user.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignInForm {
    private String email;
    private String nickName;
}
