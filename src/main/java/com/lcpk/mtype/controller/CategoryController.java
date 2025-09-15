package com.lcpk.mtype.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lcpk.mtype.dto.CategoryInfo;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.service.CategoryService;
import com.lcpk.mtype.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController { 

	private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/category/{categoryNo}")
    public String productListPage(
            @PathVariable(name="categoryNo") Long categoryNo,
            // Pageable을 파라미터로 직접 받고 기본값을 설정하는 것이 더 깔끔합니다.
            @PageableDefault(size = 20, sort = "productNo", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        
        //상품 데이터 조회
        Slice<ProductListDto> productSlice = productService.getProductList(categoryNo, pageable);
        
        //카테고리 정보 조회 (Service가 DTO로 변환해서 전달해 줌)
        CategoryInfo categoryInfo = categoryService.getCategoryInfo(categoryNo);

        //Model에 DTO 객체들을 추가
        model.addAttribute("products", productSlice);
        model.addAttribute("clickedCategory", categoryInfo.getClickedCategory());
        model.addAttribute("topCategory", categoryInfo.getTopCategory());
        model.addAttribute("midCategories", categoryInfo.getMidCategories());
        model.addAttribute("subCategories", categoryInfo.getSubCategories());
        
        return "product-list";
    }
}

//	@GetMapping({"/mbti", "/mbti/{mbti}"})
//	public String getMbtiPage(@PathVariable(name="mbti",required = false) String mbti, Model model) {
//	    List<String> mbtiGroups = List.of("E","I","S","N","T","F","J","P");
//
//	    List<CategoryEntity> categories;
//	    if (mbti != null) {
//	        categories = categoryService.getCategoryWithProductsByMbti(List.of(mbti));
//	    } else {
//	        categories = categoryService.getCategoryWithProductsByMbti(mbtiGroups);
//	    }
//
//	    // MBTI별로 그룹화
//	    Map<String, List<CategoryEntity>> categoryMap = categories.stream()
//	            .collect(Collectors.groupingBy(CategoryEntity::getMbtiName));
//
//	    model.addAttribute("categoryMap", categoryMap);
//	    model.addAttribute("mbtiGroups", mbtiGroups);
//
//	    return "product-mbti-list";
//	}

//	@GetMapping("/mbti/{categoryno}")
//	public String getMbtiCategoryPage(@PathVariable(name = "categoryno") Long categoryno, Model model) {
//		List<ProductEntity> products = productService.getProductsByCategory(categoryno);
//		model.addAttribute("products", products);
//		return "product-mbti-list";
//	}
//}
