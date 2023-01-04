package com.zerobase.everycampingbackend.user.domain.model;

import com.zerobase.everycampingbackend.common.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;    // 이메일
  private String password;    // 패스워드
  private String status;      // 회원상태
  private String address;     // 주소
  private String zipcode;     // 우편번호
  private String phone;       // 연락처

  private LocalDateTime deleteAt;  // 탈퇴일
}
