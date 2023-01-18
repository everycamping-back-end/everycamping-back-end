package com.zerobase.everycampingbackend.config;

import com.zerobase.everycampingbackend.domain.auth.filter.JwtAuthFilter;
import com.zerobase.everycampingbackend.domain.auth.model.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
        "/v2/api-docs",
        "/webjars/**"
    };

    private static final String[] AUTH_WHITELIST = {
        "/"
        , "/customers/signin"
        , "/customers/signup"
        , "/customers/reissue"
        , "/sellers/signin"
        , "/sellers/signup"
        , "/sellers/reissue"
        , "/test/**"
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
            .antMatchers("/admin/**").hasRole(UserType.ADMIN.name())
            .antMatchers("/sellers/**").hasRole(UserType.SELLER.name())
            .anyRequest().hasAnyRole(UserType.CUSTOMER.name(), UserType.SELLER.name(), UserType.ADMIN.name())
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}