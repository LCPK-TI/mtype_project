package com.lcpk.mtype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
	
	@GetMapping("/products")
	public String productList() {
		return "productList";
	}
	
	@GetMapping("/products/pest")
	public String bestProducts() {
		return "address-management";
	}
	
	@GetMapping("/products/best")
	public String pestProducts() {
		return "wishlist-mine";
	}
	
}
