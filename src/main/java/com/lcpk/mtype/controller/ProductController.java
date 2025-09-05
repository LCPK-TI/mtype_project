package com.lcpk.mtype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
	
	@GetMapping("/products")
	public String productList() {
		return "productList";
	}
	
	@GetMapping("/products/best")
	public String bestProducts() {
		return "productBest";
	}
}
