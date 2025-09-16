package com.lcpk.mtype.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.lcpk.mtype.dto.CategoryDto;
import com.lcpk.mtype.service.CategoryService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

	private final CategoryService categoryService;
	
	// 모든 페이지의 모델에 로그인 상태(isLoggedIn)를 추가하는
    @ModelAttribute("isLoggedIn")
    public boolean addIsLoggedIn(@CookieValue(name = "jwtToken", required = false) String token) {
        // jwtToken 쿠키가 존재하고 비어있지 않으면 true(로그인 상태)를 반환
        return token != null && !token.isEmpty();
    }
	
	//모든 컨트롤러 요청에서 categories를 모델에 담음
	//모든 페이지에서 카테고리 데이터 보여주기 위함
	@ModelAttribute("categories")
	public List<CategoryDto> addCategories(){
		return categoryService.getCategoryMenu();
	}
}
