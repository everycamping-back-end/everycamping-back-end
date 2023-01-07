package com.zerobase.everycampingbackend.user.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
  private String email;
  private String nickName;
  private String password;
  private String phoneNumber;
}
