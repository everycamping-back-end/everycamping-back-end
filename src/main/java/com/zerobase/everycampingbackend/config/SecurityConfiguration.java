package com.zerobase.everycampingbackend.config;

import com.zerobase.everycampingbackend.domain.auth.filter.JwtAuthFilter;
import com.zerobase.everycampingbackend.domain.auth.model.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] AUTH_IGNORELIST = {
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/webjars/**"
    };

    private static final String[] AUTH_WHITELIST = {
        "/"
        //계정 관련
        , "/customers/signin"
        , "/customers/signup"
        , "/customers/reissue"
        , "/sellers/signin"
        , "/sellers/signup"
        , "/sellers/reissue"
        , "/websocket/**"
        , "/questions"
        , "/commons/**"

        //테스트
        , "/test/**"
    };

    private static final String[] AUTH_ALLOW_GET_LIST = {
        // 비회원 조회 가능한 요소들
        "/products/**" //상품 조회
        , "/reviews/**" //리뷰 조회
    };

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(AUTH_IGNORELIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.GET, AUTH_ALLOW_GET_LIST).permitAll()
            .antMatchers("/admin/**").hasRole(UserType.ADMIN.name())
            .antMatchers("/sellers/**").hasRole(UserType.SELLER.name())
            .anyRequest().hasAnyRole(UserType.CUSTOMER.name(), UserType.SELLER.name(), UserType.ADMIN.name())
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
