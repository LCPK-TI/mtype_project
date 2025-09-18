package com.lcpk.mtype.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.ProductImgEntity;

import lombok.Getter;

@Getter
public class ProductDetailDto { // 상품 상세페이지용 dto

	private final Long productNo;
	private final String productName;
	private final int productPrice;
	private final String detailImgUrl;

	private final CategoryDto category; // 상품의 카테고리 정보
	private final CategoryDto topCategory; // 최상위 카테고리 정보

	private final String mainImageUrl; // 대표 이미지
	private final List<String> subImageUrls; // 나머지 서브 이미지 목록

	private List<OptionGroup> optionGroups; // 옵션 정보. 화면에 옵션 버튼들을 그리기 위한 정보
	private List<SkuInfo> skus; // sku 정보. js가 선택된 옵션 조합으로 최종 skuNo를 찾기 위한 역할

	// 생성자는 모든 정보를 받아서 필드를 초기화합니다.
	public ProductDetailDto(ProductEntity product, CategoryEntity topCategory, String mainImageUrl,
			List<String> subImageUrls, List<OptionGroup> optionGroups, List<SkuInfo> skus) {
		this.productNo = product.getProductNo();
		this.productName = product.getProductName();
		this.productPrice = product.getProductPrice();
		this.detailImgUrl = product.getDetailImgUrl();

		this.category = new CategoryDto(product.getCategory());
		this.topCategory = new CategoryDto(topCategory);

		// 이미지 리스트에서 대표 이미지와 서브 이미지를 분리
		this.mainImageUrl = product.getImages().stream().filter(img -> "Y".equals(img.getIsMain()))
				.map(ProductImgEntity::getImgUrl).findFirst().orElse(null); // 대표 이미지가 없을 경우 null

		this.subImageUrls = product.getImages().stream().filter(img -> "N".equals(img.getIsMain()))
				.map(ProductImgEntity::getImgUrl).collect(Collectors.toList());
	}
}
