package com.lcpk.mtype.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.ProductImgEntity;
import com.lcpk.mtype.service.CategoryService;
import com.lcpk.mtype.service.ProductImgService;
import com.lcpk.mtype.service.ProductService;


@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private	CategoryService categoryservice;
	@Autowired
	private ProductImgService productImgService;
	
	//상품 상세 페이지
	@GetMapping("{productno}")
	public String getProductPage(@PathVariable(name="productno") Long productno, Model model) {
		
		ProductEntity product = productService.findById(productno).orElse(null);
		//후보 이미지 (is_main이 n이 인것)
		List<ProductImgEntity> images = productImgService.findByProduct_ProductNo(productno)
				.stream()
				.filter(img->"N".equals(img.getIsMain()))
				.collect(Collectors.toList());
		model.addAttribute("imgs",images);
		model.addAttribute("product",product);
		if(categoryservice.findTopParent(product.getCategoryNo()).getCategoryNo()==3L) { //도서인 경우
			
			return "book-detail";
		}else {
			return "product-detail";
		}
	}
	
	@GetMapping("/best")
	public String best(Model model) {
		return "product-best";
	}
}
