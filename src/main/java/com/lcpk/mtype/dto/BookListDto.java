package com.lcpk.mtype.dto;

import lombok.Getter;

@Getter
public class BookListDto {
	private final Long isbn;   //도서 isbn
	private final String title;  //도서명
	private final String publisher; //출판사
	private final Long price;  //도서가격
	private final String imgUrl; // 대표 이미지 URL

	// JPQL에서 바로 DTO를 생성하기 위한 생성자
	public BookListDto(Long isbn, String title, String publisher, Long price, String imgUrl) {
		this.isbn = isbn;
		this.title = title;
		this.publisher = publisher;
		this.price = price;
		this.imgUrl = imgUrl;
	}
}
