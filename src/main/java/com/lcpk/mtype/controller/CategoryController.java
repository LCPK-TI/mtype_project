package com.lcpk.mtype.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lcpk.mtype.dto.BookListDto;
import com.lcpk.mtype.dto.CategoryInfo;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.service.BookService;
import com.lcpk.mtype.service.CategoryService;
import com.lcpk.mtype.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController { 

	private final ProductService productService;
	private final BookService bookService;
    private final CategoryService categoryService;
    private static final long BOOK_CATEGORY_NO = 3L;
    
    @GetMapping("/category/{categoryNo}")
    public String productListPage(
            @PathVariable(name="categoryNo") Long categoryNo,
            @PageableDefault(size = 20) Pageable pageable,
            Model model
    ) {
    	
    	//카테고리 정보 조회 (Service가 DTO로 변환해서 전달해 줌)
        CategoryInfo categoryInfo = categoryService.getCategoryInfo(categoryNo);

        //Model에 DTO 객체들을 추가
        model.addAttribute("clickedCategory", categoryInfo.getClickedCategory());
        model.addAttribute("topCategory", categoryInfo.getTopCategory());
        model.addAttribute("midCategories", categoryInfo.getMidCategories());
        model.addAttribute("subCategories", categoryInfo.getSubCategories());
      //상품 카테고리가 도서인 경우
    	if(categoryNo==BOOK_CATEGORY_NO||categoryService.findTopParent(categoryNo).getCategoryNo()==BOOK_CATEGORY_NO) {
    		pageable = PageRequest.of(0, 20,Sort.by("isbn").descending());
    		//도서 데이터 조회
    		Slice<BookListDto> bookSlice = bookService.getBookList(categoryNo, pageable);
    		model.addAttribute("products", bookSlice);
    		return "book-list";
    	}else {//상품 카테고리가 도서가 아닌 경우
    		pageable = PageRequest.of(0, 20,Sort.by("productNo").descending());
    		 //상품 데이터 조회
            Slice<ProductListDto> productSlice = productService.getProductList(categoryNo, pageable);
            model.addAttribute("products", productSlice);
            return "product-list";
    	}
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
