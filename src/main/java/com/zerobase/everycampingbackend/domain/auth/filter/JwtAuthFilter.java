package com.zerobase.everycampingbackend.domain.auth.filter;

import com.zerobase.everycampingbackend.domain.auth.entrypoint.CustomBasicAuthenticationEntryPoint;
import com.zerobase.everycampingbackend.domain.auth.provider.JwtAuthenticationProvider;
import com.zerobase.everycampingbackend.exception.CustomException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    public static final String JWT_HEADER_KEY = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        boolean isWebSocketHandShakingRequest = request.getRequestURL().toString().contains("websocket");
        if (isWebSocketHandShakingRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveTokenFromRequest(request);

        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if(jwtAuthenticationProvider.validateToken(token)){
                Authentication auth = jwtAuthenticationProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e){
            customBasicAuthenticationEntryPoint.commence(request, response, e);
        }
    }

    private String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(JWT_HEADER_KEY);

        if(!ObjectUtils.isEmpty(token)) {
            return token;
        }

        return null;
    }
}
