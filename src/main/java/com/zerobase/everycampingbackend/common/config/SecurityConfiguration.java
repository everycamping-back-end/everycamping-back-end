package com.zerobase.everycampingbackend.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring().antMatchers("/favicon.ico", "files/**");

    super.configure(web);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

    //

    http.authorizeRequests()
        .antMatchers("/"
            , "/customers/signin"
            , "/customers/signup"
            , "/sellers/signin"
            , "/sellers/signup"
        )
            .permitAll();

    // seller/** 페이지는 ROLE_SELLER 권한이 있어야 접근 가능하게 설정

    /*
    http.authorizeRequests()
        .antMatchers("/sellers/**")
            .hasAuthority("ROLE_SELLER");

    http.authorizeRequests()
        .antMatchers("/admin/**")
            .hasAuthority("ROLE_ADMIN");
     */



    super.configure(http);
  }
}
