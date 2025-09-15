package com.lcpk.mtype.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.lcpk.mtype.dto.CategoryDto;
import com.lcpk.mtype.service.CategoryService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

	private final CategoryService categoryService;
	
	//모든 컨트롤러 요청에서 categories를 모델에 담음
	//모든 페이지에서 카테고리 데이터 보여주기 위함
	@ModelAttribute("categories")
	public List<CategoryDto> addCategories(){
		return categoryService.getCategoryMenu();
	}
}
