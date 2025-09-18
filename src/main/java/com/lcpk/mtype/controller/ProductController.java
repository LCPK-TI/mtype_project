package com.lcpk.mtype.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcpk.mtype.dto.ProductDetailDto;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.service.ProductService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class ProductController {
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;
	private static final long BOOK_CATEGORY_NO = 3L;
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
	public String getProductPage(@PathVariable("productNo") Long productNo, Model model) throws JsonProcessingException {
        // Service를 호출하여 DTO를 받음(상품명, 옵션그룹, SKU 목록 등)
        ProductDetailDto productDto = productService.getProductDetail(productNo);

        //Model에 DTO를 추가
        model.addAttribute("product", productDto);
        System.out.println("Main Image URL: " + productDto.getMainImageUrl());
        System.out.println("Sub Image URLs: " + productDto.getSubImageUrls());
        
        //SKUS 목록을 JSON 문자열로 변환해서 따로 담아줌
        ObjectMapper objectMapper = new ObjectMapper();
        String skusJson = objectMapper.writeValueAsString(productDto.getSkus());
        model.addAttribute("skusJson",skusJson);
        //DTO에 담긴 최상위 카테고리 ID로 View를 결정
        if (productDto.getTopCategory().getCategoryNo() == BOOK_CATEGORY_NO) { // 3L은 상수로 관리하는 것이 더 좋습니다.
            return "book-detail";
        } else {
            return "product-detail";
        }
    }
	@GetMapping("/best")
	public String best(Model model) {
		return "product-best";
	}
	// 키워드 검색 결과 응답용 API
	@GetMapping("/api/search/products")
	@ResponseBody
	public ResponseEntity<Slice<ProductListDto>> searchProductsApi(@RequestParam("query") String query, Pageable pageable){
		return ResponseEntity.ok(productService.searchProductsByKeyword(query, pageable));
	}
	
	// 키워드 검색 결과 페이지
	@GetMapping("/product/search")
	public String searchProductPage(@RequestParam("query") String query, Pageable pageable, Model model) {
		Slice<ProductListDto> products = productService.searchProductsByKeyword(query, pageable);
		model.addAttribute("products", products);
		model.addAttribute("query", query);
		return "product-search";
	}
}
