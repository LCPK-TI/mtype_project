package com.lcpk.mtype.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lcpk.mtype.dto.ProductDetailDto;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	// JSON 데이터를 응답하는 API 메서드
	@GetMapping("/api/products")
	@ResponseBody
	public ResponseEntity<Slice<ProductListDto>> getProductsApi(@RequestParam(value="categoryNo", required = false) Long categoryNo,
			Pageable pageable) {
		Slice<ProductListDto> productSlice = productService.getProductList(categoryNo, pageable);
		return ResponseEntity.ok(productSlice); // JSON 데이터를 반환
	}

//	 상품 상세 페이지
	@GetMapping("/product/{productNo}")
	public String getProductPage(@PathVariable("productNo") Long productNo, Model model) {
        // 1. Service를 호출하여 DTO를 받음
        ProductDetailDto productDto = productService.getProductDetail(productNo);

        // 2. Model에 DTO를 추가
        model.addAttribute("product", productDto);
        System.out.println("Main Image URL: " + productDto.getMainImageUrl());
        System.out.println("Sub Image URLs: " + productDto.getSubImageUrls());
        // 3. DTO에 담긴 최상위 카테고리 ID로 View를 결정
        if (productDto.getTopCategory().getCategoryNo() == 3L) { // 3L은 상수로 관리하는 것이 더 좋습니다.
            return "book-detail";
        } else {
            return "product-detail";
        }
    }
	
	@GetMapping("/best")
	public String best(Model model) {
		return "product-best";
	}
}
