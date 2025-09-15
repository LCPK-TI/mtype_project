package com.lcpk.mtype.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lcpk.mtype.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/user/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/user/login-success")
	public String loginSuccessPage() {
		return "login-success";
	}
	
	@GetMapping("/user/mypage")
	public String userMypage() {
		return "user-mypage";
	}
	@GetMapping("/user/detail")
	public String userDetail() {
		return "user-detail";
	}
	
	@DeleteMapping("/api/user/withdraw")
	@ResponseBody
	public ResponseEntity<String> withdrawUser(@RequestHeader("Authorization") String accessToken){
		String token = accessToken.substring(7);
		userService.withdrawUser(token);
		return ResponseEntity.ok("회원탈퇴에 성공했습니다.");
	}
	
	@PostMapping("/api/user/reactivate")
	@ResponseBody
	public ResponseEntity<String> reactivateUser(@RequestBody Map<String, Long> payload) {
		Long kakaoUserId = payload.get("kakaoUserId");
		String newJwtToken = userService.reactivateUserAndCreateToken(kakaoUserId);
		return ResponseEntity.ok(newJwtToken);
	}
	
}
