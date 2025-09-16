package com.lcpk.mtype.dto;

import java.util.List;
import java.util.Map;
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

	private List<OptionGroup> optionGroups;
	private List<SkuInfo> skus;

	// 생성자는 모든 정보를 받아서 필드를 초기화합니다.
	private ProductDetailDto(ProductEntity product, CategoryEntity topCategory, String mainImageUrl,
			List<String> subImageUrls, List<OptionGroup> optionGroups, List<SkuInfo> skus) {
		this.productNo = product.getProductNo();
		this.productName = product.getProductName();
		this.productPrice = product.getProductPrice();
		this.detailImgUrl = product.getDetailImgUrl();
		this.category = new CategoryDto(product.getCategory());
		this.topCategory = new CategoryDto(topCategory);
		this.mainImageUrl = mainImageUrl;
		this.subImageUrls = subImageUrls;
		this.optionGroups = optionGroups;
		this.skus = skus;
	}

	public static ProductDetailDto from(ProductEntity product, CategoryEntity topCategory,
			List<OptionGroup> optionGroups, List<SkuInfo> skus) {

		// 이미지 분리 로직
		Map<Boolean, List<String>> partitionedImages = product.getImages().stream()
				.collect(Collectors.partitioningBy(img -> "Y".equals(img.getIsMain()),
						Collectors.mapping(ProductImgEntity::getImgUrl, Collectors.toList())));
		String mainImg = partitionedImages.get(true).stream().findFirst().orElse(null);
		List<String> subImgs = partitionedImages.get(false);

		// private 생성자를 통해 모든 정보를 담은 DTO 객체 생성
		return new ProductDetailDto(product, topCategory, mainImg, subImgs, optionGroups, skus);
	}
}
