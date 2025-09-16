package com.lcpk.mtype.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcpk.mtype.config.jwt.JwtTokenProvider;
import com.lcpk.mtype.entity.User;
import com.lcpk.mtype.entity.YesOrNo;
import com.lcpk.mtype.exception.WithdrawnUserException;
import com.lcpk.mtype.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoService {
	
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final RestTemplate restTemplate;
	
	@Value("${kakao.client-id}")
	private String KAKAO_CLIENT_ID;
	
	@Value("${kakao.redirect-uri}")
	private String KAKAO_REDIRECT_URI;
	
	@Transactional
	public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
		String accessToken = getAccessToken(code);	// 인가코드로 access Token 요청
		JsonNode userInfo = getUserInfo(accessToken);	// accessToken으로 사용자 정보 요청
		User user = processKakaoUser(userInfo);	// 사용자 정보와 db 확인 => 회원가입 or 로그인
		
		// 토큰 생성
		String jwtToken = jwtTokenProvider.createToken(user.getKakaoUserId());
		
		// 쿠키 생성
		Cookie cookie = new Cookie("jwtToken", jwtToken);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60*60); // 쿠키 유효시간
		
		response.addCookie(cookie);
	}
	
	private String getAccessToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", KAKAO_CLIENT_ID);
		body.add("redirect_uri", KAKAO_REDIRECT_URI);
		body.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
		
		HttpEntity<String> response = restTemplate.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(response.getBody()).get("access_token").asText();
	}
	
	private JsonNode getUserInfo(String accessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoUserInfoRequest,
				String.class
		);
		
		return new ObjectMapper().readTree(response.getBody());
	}
	
	private User processKakaoUser(JsonNode userInfo) {
		// 카카오에게 받은 사용자 정보 추출
		Long kakaoId = userInfo.get("id").asLong();
		String nickname = userInfo.get("properties").get("nickname").asText();
		String email = userInfo.get("kakao_account").has("email") ? userInfo.get("kakao_account").get("email").asText() : null;
		
		if(email == null) {
			throw new IllegalArgumentException("카카오 이메일을 가져올 수 없습니다. 동의 항목을 확인해주세요.");
		}
		
		//DB에 이미 존재하는 사용자면 해당 사용자값 리턴, 없다면 정보 추가
		User user = userRepository.findByKakaoUserId(kakaoId).orElseGet(() -> {
            User newUser = User.builder()
                    .kakaoUserId(kakaoId).userName(nickname)
                    .userEmail(email).userJoinDate(new Date()).build();
            return userRepository.save(newUser);
        });
		
		if(user.getUserIsWithdrawn() == YesOrNo.Y) {
			throw new WithdrawnUserException("이미 탈퇴한 회원입니다.", user.getKakaoUserId());
		}
		return user;
	}
}
