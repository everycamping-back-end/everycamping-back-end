package com.zerobase.everycampingbackend.config;

import com.zerobase.everycampingbackend.domain.auth.entrypoint.CustomBasicAuthenticationEntryPoint;
import com.zerobase.everycampingbackend.domain.auth.filter.JwtAuthFilter;
import com.zerobase.everycampingbackend.domain.auth.handler.AuthFailureHandler;
import com.zerobase.everycampingbackend.domain.auth.type.UserType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final AuthFailureHandler authFailureHandler;

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
        , "/customers/signin/social/**"
        , "/customers/signup"
        , "/customers/reissue"
        , "/sellers/signin"
        , "/sellers/signup"
        , "/sellers/reissue"
        , "/websocket/**"
        , "/chat-rooms/**"
        , "/commons/**"
        , "/admins/signup"
        , "/admins/signin"
        , "/admins/reissue"

        //헬스체크
        , "/health"
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
            .formLogin().disable()
            .httpBasic().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.GET, AUTH_ALLOW_GET_LIST).permitAll()
            .antMatchers("/admin/**").hasRole(UserType.ADMIN.name())
            .antMatchers("/sellers/**", "/manage/products/**").hasRole(UserType.SELLER.name())
            .antMatchers("/customers/**").hasRole(UserType.CUSTOMER.name())
            .anyRequest().hasAnyRole(UserType.CUSTOMER.name(), UserType.SELLER.name(), UserType.ADMIN.name())
            .and()
            .logout().logoutSuccessUrl("/")
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//            .oauth2Login(e -> e
//                .userInfoEndpoint().userService(customOAuth2UserService)
//                .and()
//                .successHandler(oAuth2SuccessHandler(customerService))
//            )
            .exceptionHandling()
            .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
            .accessDeniedHandler(authFailureHandler);
    }

//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler(CustomerService customerService) {
//        return new OAuth2SuccessHandler(customerService);
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://everycamping.netlify.app"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
