package com.zerobase.everycampingbackend.user.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.common.token.config.JwtAuthenticationProvider;
import com.zerobase.everycampingbackend.common.token.model.UserType;
import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import com.zerobase.everycampingbackend.user.domain.form.SignInForm;
import com.zerobase.everycampingbackend.user.domain.form.SignUpForm;
import com.zerobase.everycampingbackend.user.domain.repository.SellerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {
  private final SellerRepository sellerRepository;
  private final JwtAuthenticationProvider provider;

  public String signUp(SignUpForm form){
    Seller seller = sellerRepository.save(Seller.from(form));

    return "판매자 회원 가입에 성공하였습니다.";
  }

  public String signIn(SignInForm form){
    // 로그인 가능 여부 체크
    Seller s = this.findValidSeller(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

    return provider.createToken(s.getEmail(), s.getId(), UserType.SELLER);
  }


  private Optional<Seller> findValidSeller(String email, String password){
    return sellerRepository.findByEmail(email).stream()
        .filter(seller -> seller.getPassword().equals(password))
        .findFirst();
  }

  public Optional<Seller> findByIdAndEmail(Long id, String email){
    return sellerRepository.findById(id)
        .stream().filter(seller -> seller.getEmail().equals(email))
        .findFirst();
  }
}
