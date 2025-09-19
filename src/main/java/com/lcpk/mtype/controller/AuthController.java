package com.lcpk.mtype.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcpk.mtype.exception.WithdrawnUserException;
import com.lcpk.mtype.service.KakaoService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final KakaoService kakaoService;
	
	@GetMapping("/oauth/kakao/redirect")
	public void kakaoRedirect(@RequestParam("code") String code, HttpServletResponse response) throws IOException{
		try {
			kakaoService.kakaoLogin(code, response);
			response.sendRedirect("/");
			
		} catch (WithdrawnUserException e) {
			// 탈퇴 회원의 로그인 시도
			Long kakaoId = e.getKakaoUserId();
			response.sendRedirect("http://localhost:8888/user/login?status=withdrawn&id="+kakaoId);
		} catch (Exception e) {
			String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
			response.sendRedirect("http://localhost:8888/user/login?error="+errorMessage);
		}
		
		
		
		
	}
}
