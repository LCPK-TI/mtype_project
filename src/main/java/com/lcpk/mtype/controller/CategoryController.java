package com.lcpk.mtype.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.service.CategoryService;
import com.lcpk.mtype.service.ProductService;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	//카테고리 클릭 시 상품 리스트 페이지
	@GetMapping("/{categoryno}") //페이지 렌더링
	public String getCategoryPage(@PathVariable(name="categoryno") Long categoryno,Model model) {
		//상품 조회 (클릭한 카테고리를 포함한 하위 카테고리 상품 전체 조회)
		List<ProductEntity> products = productService.getProductsByCategory(categoryno);
		model.addAttribute("products",products);
		
		CategoryEntity clicked = categoryService.findById(categoryno);
		CategoryEntity topCategory = null;
		List<CategoryEntity> midCategories = List.of();
		List<CategoryEntity> subCategories = List.of();
		
		//최상위 클릭했을 때
		if(clicked.getDepth()==1) {
			topCategory = clicked;
			midCategories = categoryService.findChildren(clicked.getCategoryNo());
		}
		//중간 카테고리 클릭했을 때
		else if(clicked.getDepth()==2) {
			topCategory = categoryService.findById(clicked.getParentCategoryNo());
			midCategories = categoryService.findChildren(topCategory.getCategoryNo());
			subCategories = categoryService.findChildren(clicked.getCategoryNo());
		}
		//하위 카테고리 클릭했을 때
		else if(clicked.getDepth()==3) {
			CategoryEntity midCategory = categoryService.findById(clicked.getParentCategoryNo());
			topCategory = categoryService.findTopParent(categoryno);
			midCategories = categoryService.findChildren(topCategory.getCategoryNo());
			subCategories = categoryService.findChildren(midCategory.getCategoryNo());
		}
		//model 세팅
		model.addAttribute("topCategory",topCategory); //최상위
		model.addAttribute("midCategories",midCategories); //상위
		model.addAttribute("subCategories",subCategories); //하위
		model.addAttribute("clickedCategory",clicked); //클릭한카테고리
		return "product-list";
	}
}
