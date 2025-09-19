package com.lcpk.mtype.dto;

import com.lcpk.mtype.entity.SellerEntity;

import lombok.Getter;

@Getter
public class SellerDto {
	private Long sellerNo;
	private String sellerNm; //대표자
	private String sellerTel; //대표자 전화번호
	private String businessNo; //사업자 번호
	private String businessNm; //상호명
	
	public SellerDto(SellerEntity seller) {
        this.sellerNo = seller.getSellerNo();
        this.sellerNm = seller.getSellerNm();
        this.sellerTel = seller.getSellerTel();
    }
}
