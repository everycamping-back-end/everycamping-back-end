package com.zerobase.everycampingbackend.common.auth.filter;

import com.zerobase.everycampingbackend.common.auth.provider.JwtAuthenticationProvider;
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
    public static final String JWT_HEADER_KEY = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = resolveTokenFromRequest(request);

        if(StringUtils.hasText(token) && jwtAuthenticationProvider.validateToken(token)){
            Authentication auth = jwtAuthenticationProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(JWT_HEADER_KEY);

        if(!ObjectUtils.isEmpty(token)) {
            return token;
        }

        return null;
    }
}
