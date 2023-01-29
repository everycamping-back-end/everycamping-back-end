package com.zerobase.everycampingbackend.domain.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.everycampingbackend.domain.auth.dto.CustomOAuth2User;
import com.zerobase.everycampingbackend.domain.auth.dto.JwtDto;
import com.zerobase.everycampingbackend.domain.auth.dto.OAuthAttributes;
import com.zerobase.everycampingbackend.domain.user.service.CustomerService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        if(!(authentication instanceof OAuth2AuthenticationToken)){
            return;
        }

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        OAuthAttributes attributes = user.getOAuthAttributes();

        JwtDto jwt = customerService.socialSignIn(attributes.getEmail(), attributes.getName());
        ResponseEntity<JwtDto> responseEntity = ResponseEntity.ok(jwt);

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, responseEntity);
        writer.flush();
        writer.close();
    }
}
