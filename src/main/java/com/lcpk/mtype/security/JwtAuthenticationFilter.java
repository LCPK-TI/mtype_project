package com.lcpk.mtype.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
    	String token = null;

    	// 쿠키에서 accessToken 추출
    	if (request.getCookies() != null) {
    	    for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
    	        if ("accessToken".equals(cookie.getName())) {
    	            token = cookie.getValue();
    	            break;
    	        }
    	    }
    	}

    	// 토큰 검증 및 인증 처리
    	if (token != null && jwtUtil.validateToken(token)) {
    	    String sellerId = jwtUtil.getSellerIdFromToken(token);

    	    // 사용자 인증 정보 생성
    	    User principal = new User(sellerId, "", java.util.List.of(new SimpleGrantedAuthority("ROLE_SELLER")));
    	    UsernamePasswordAuthenticationToken authentication =
    	            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    	    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	    SecurityContextHolder.getContext().setAuthentication(authentication);	// 인증 등록
    	}

    	chain.doFilter(request, response);


    }
}
