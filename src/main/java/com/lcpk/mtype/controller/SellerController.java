package com.lcpk.mtype.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lcpk.mtype.entity.SellerEntity;
import com.lcpk.mtype.security.JwtUtil;
import com.lcpk.mtype.service.SellerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sellers")
public class SellerController {

	private final SellerService sellerService;
	private final JwtUtil jwtUtil; // JwtUtil 필드 추가

	@Autowired
	public SellerController(SellerService sellerService, JwtUtil jwtUtil) {
		this.sellerService = sellerService;
		this.jwtUtil = jwtUtil;
	}

	// 회원가입 페이지 요청
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("sellerEntity", new SellerEntity());
		return "seller-reg";
	}

	// 회원가입 처리
	@PostMapping("/signup")
	public String signup(@ModelAttribute SellerEntity sellerEntity, @RequestParam("checkPw") String checkPw,
			Model model) {
		if (!sellerService.isValidSellerPw(sellerEntity.getSellerPw())) {
			model.addAttribute("error", "비밀번호는 영문자, 숫자, 특수문자 조합의 8~12자여야 합니다.");
			model.addAttribute("sellerEntity", sellerEntity);
			return "seller-reg";
		}

		if (!sellerEntity.getSellerPw().equals(checkPw)) {
			model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			model.addAttribute("sellerEntity", sellerEntity);
			return "seller-reg";
		}

		if (sellerService.isDuplicateSellerId(sellerEntity.getSellerId())) {
			model.addAttribute("error", "이미 사용 중인 아이디입니다.");
			model.addAttribute("sellerEntity", sellerEntity);
			return "seller-reg";
		}

		sellerService.registerSeller(sellerEntity);
		return "redirect:/sellers/login";
	}

	// 아이디 중복 확인
	@GetMapping("/check-id")
	@ResponseBody
	public Map<String, Boolean> checkDuplicatedId(@RequestParam("sellerId") String sellerId) {
		boolean isDuplicate = sellerService.isDuplicateSellerId(sellerId);
		Map<String, Boolean> result = new HashMap<>();
		result.put("duplicate", isDuplicate);
		return result;
	}

	// 로그인 페이지 요청
	@GetMapping("/login")
	public String loginPage() {
		return "seller-login";
	}

	// 로그인 처리
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("sellerId") String sellerId,
	                               @RequestParam("sellerPw") String sellerPw,
	                               HttpServletResponse response) {
	    boolean success = sellerService.login(sellerId, sellerPw);

	    if (!success) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다."));
	    }

	    String accessToken = jwtUtil.createAccessToken(sellerId);
	    String refreshToken = jwtUtil.createRefreshToken(sellerId);

	    sellerService.updateRefreshToken(sellerId, refreshToken);
	    
	    ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(Duration.ofMinutes(30))
	            .sameSite("Strict")
	            .build();

	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(Duration.ofDays(7))
	            .sameSite("Strict")
	            .build();

	    // 헤더에 Set-Cookie 추가
	    return ResponseEntity.status(HttpStatus.FOUND)
	            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
	            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
	            .header(HttpHeaders.LOCATION, "/sellers/index")
	            .build();
	}
	
	// RefreshToken 재발급 API
	@PostMapping("/refresh")
	@ResponseBody
	public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
	    // 쿠키에서 refreshToken 추출
	    String refreshToken = null;
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if ("refreshToken".equals(cookie.getName())) {
	                refreshToken = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "유효하지 않은 RefreshToken입니다."));
	    }

	    String sellerId = jwtUtil.getSellerIdFromToken(refreshToken);

	    Optional<SellerEntity> sellerOpt = sellerService.findBySellerId(sellerId);
	    if (sellerOpt.isEmpty() || !refreshToken.equals(sellerOpt.get().getRefreshToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "재인증이 필요합니다."));
	    }

	    // 새 accessToken 및 refreshToken 생성
	    String newAccessToken = jwtUtil.createAccessToken(sellerId);
	    String newRefreshToken = jwtUtil.createRefreshToken(sellerId);

	    // DB에 새 refreshToken 저장
	    sellerService.updateRefreshToken(sellerId, newRefreshToken);

	    // 쿠키 설정
	    ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(Duration.ofMinutes(30))
	            .sameSite("Strict")
	            .build();

	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(Duration.ofDays(7))
	            .sameSite("Strict")
	            .build();

	    return ResponseEntity.ok()
	            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
	            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
	            .body(Map.of("message", "토큰 재발급 완료"));    
	}
	
	// 로그아웃 API
	@PostMapping("/logout")
	@ResponseBody
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
	    String sellerId = null;

	    // accessToken에서 sellerId 추출
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if ("accessToken".equals(cookie.getName())) {
	                String accessToken = cookie.getValue();
	                if (jwtUtil.validateToken(accessToken)) {
	                    sellerId = jwtUtil.getSellerIdFromToken(accessToken);
	                }
	                break;
	            }
	        }
	    }

	    // DB에서 RefreshToken 제거
	    if (sellerId != null) {
	        sellerService.clearRefreshToken(sellerId);
	    }

	    // 쿠키 삭제
	    ResponseCookie deleteAccessCookie = ResponseCookie.from("accessToken", "")
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(0)
	            .sameSite("Strict")
	            .build();

	    ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "")
	            .httpOnly(true)
	            .secure(false)
	            .path("/")
	            .maxAge(0)
	            .sameSite("Strict")
	            .build();
	    
	    // 로그아웃 후 로그인 페이지로 이동
	    return ResponseEntity.status(HttpStatus.SEE_OTHER)
	            .header(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString())
	            .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString())
	            .header(HttpHeaders.LOCATION, "/sellers/login")
	            .build();
	}

	
	// 메인페이지
	@GetMapping("/index")
	public String index() {
		return "index";
	}
}
