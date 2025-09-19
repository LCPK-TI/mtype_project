package com.lcpk.mtype.dto;

import lombok.Getter;

@Getter
public class ProductListDto {
    private final Long productNo;
    private final String productName;
   // private final String storeName; // 스토어명 추가
    private final int productPrice;
    private final String mainImageUrl; // 대표 이미지 URL 추가

    // JPQL에서 바로 DTO를 생성하기 위한 생성자
    public ProductListDto(Long productNo, String productName,  int productPrice, String mainImageUrl) {//String storeName,
        this.productNo = productNo;
        this.productName = productName;
     //   this.storeName = storeName;
        this.productPrice = productPrice;
        this.mainImageUrl = mainImageUrl;
    }
}