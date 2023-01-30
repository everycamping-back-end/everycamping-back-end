package com.zerobase.everycampingbackend.domain.auth.entrypoint;

import com.zerobase.everycampingbackend.exception.CustomException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {
        handlerExceptionResolver.resolveException(request, response, null, authException);
    }

    public void commence(HttpServletRequest request, HttpServletResponse response,
        CustomException exception) {
        handlerExceptionResolver.resolveException(request, response, null, exception);
    }
}
