package com.lcpk.mtype.dto;

import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.RecentViewEntity;

import lombok.Getter;

// 프론트 측에 최근 본 상품 정보를 전달하기 위한 용도.
@Getter
public class RecentViewDto {
	
	private final Long productNo;
	private final String thumbnailUrl;
	private final String storeName;
	private final String productName;
	private final int productPrice;
	
	public RecentViewDto(RecentViewEntity recentViewEntity) {
		ProductEntity productEntity = recentViewEntity.getProductEntity();
		
		this.productNo = productEntity.getProductNo();
		this.thumbnailUrl = productEntity.getThumbnailUrl();
		this.productName = productEntity.getProductName();
		this.productPrice = productEntity.getProductPrice();
		this.storeName = "임시스토어명";
	}
}
