package com.lcpk.mtype.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.dto.ProductDetailDto;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	private static final int PAGE_SIZE = 20;

	public Slice<ProductListDto> getProductList(Long categoryNo, Pageable pageable) {
		if (categoryNo == null) {
			return productRepository.findAllProjectedBy(pageable);
		} else {
			// CategoryService를 통해 자신 및 모든 하위 카테고리 No 목록을 가져오기
			List<Long> categoryNos = categoryService.findAllSubCategoryIds(categoryNo);

			//no 목록을 Repository에 전달하여 상품 조회
			return productRepository.findByCategoryNosIn(categoryNos, pageable);
		}
	}
	public ProductDetailDto getProductDetail(Long productNo) {
        // Repository의 fetch join 쿼리를 호출하여 ProductEntity를 조회
        ProductEntity product = productRepository.findProductWithDetailsByProductNo(productNo)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productNo));

        // 2. 최상위 부모 카테고리 조회
        CategoryEntity topCategory = categoryService.findTopParent(product.getCategory().getCategoryNo());
        
        //Entity를 DTO로 변환하여 반환
        return new ProductDetailDto(product, topCategory);
    }
	
	
	// 키워드 검색용 메소드
	public Slice<ProductListDto> searchProductsByKeyword(String query, Pageable pageable){
		return productRepository.findByProductNameContaining(query, pageable);
	}
}
