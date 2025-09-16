package com.lcpk.mtype.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lcpk.mtype.dto.RecentViewDto;
import com.lcpk.mtype.service.RecentViewService;
import com.lcpk.mtype.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	private final RecentViewService recentViewService;
	
	@GetMapping("/user/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/user/logout")
    public String logout(HttpServletResponse response) {
        // 만료 시간을 0으로 설정한 쿠키를 생성하여 기존 쿠키를 덮어씀.
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        // 메인 페이지로
        return "redirect:/";
    }
	
	/*
	@GetMapping("/user/login-success")
	public String loginSuccessPage() {
		return "login-success";
	}
	*/
	
	@GetMapping("/user/mypage")
	public String userMypage(@CookieValue(name = "jwtToken", required = false) String token, Model model) {
		List<RecentViewDto> recentProductsPreview = new ArrayList<>();
		
		// 토큰 존재시
		if (token != null) {
            // 서비스 호출 시 쿠키에서 읽은 토큰 값을 그대로 사용
            recentProductsPreview = recentViewService.getRecentViewPreview(token);
        }
		model.addAttribute("recentProductsPreview", recentProductsPreview);
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
	
	@GetMapping("/user/recent-views")
	public String recentViewsPage(@CookieValue(name = "jwtToken", required = false) String token, Model model) {
		List<RecentViewDto> recentProducts = new ArrayList<>();
		
		if(token != null) {
			// 토큰이 존재하면
			recentProducts = recentViewService.getRecentViews(token);
		}
		
		model.addAttribute("products", recentProducts);
		return "user-recent-view-product";
	}
}
