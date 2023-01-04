package com.zerobase.everycampingbackend.user.domain.model;

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
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;    // 이메일
  private String password;    // 패스워드
  private String status;      // 회원상태
  private String address;     // 주소
  private String zipcode;     // 우편번호
  private String phone;       // 연락처

  private LocalDateTime createdAt; // 가입일
  private LocalDateTime updatedAt; // 수정일
  private LocalDateTime deleteAt;  // 탈퇴일
}
