package com.lcpk.mtype.config.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	// JWT 생성 및 관리
	
	private final Key key;
	private final long tokenValidityInMilliseconds;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.tokenValidityInMilliseconds = 1000L*60*60*1;	// 1시간 유효시간
	}
	
	public String createToken(Long kakaoUserId) {
		Date now = new Date();	// 현재시간
		Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds); // 유효시간
		
		return Jwts.builder()
				.setSubject(String.valueOf(kakaoUserId))
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	// 토큰 값을 가져와 카카오 회원 번호 알아냄.
	public Long getKakaoUserIdFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key).build()
				.parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}
}
