package com.zerobase.everycampingbackend.domain.user.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForm {
    private String oldPassword;
    private String newPassword;
}
