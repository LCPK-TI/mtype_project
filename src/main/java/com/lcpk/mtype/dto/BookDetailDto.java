package com.lcpk.mtype.dto;

import java.util.Date;

import com.lcpk.mtype.entity.BookEntity;
import com.lcpk.mtype.entity.CategoryEntity;

import lombok.Getter;

@Getter
public class BookDetailDto {
	private final Long isbn;
	private final CategoryDto category;
	private final CategoryDto topCategory; // 최상위 카테고리 정보
	private final String title;
	private final String imgUrl;
	private final String author;
	private final Long price;
	private final String publisher;
	private final Date pubDate;
	private final String description;
	private final String contents;
	private final String author_intro;
	private final Long stock;
	
	public BookDetailDto(BookEntity book, CategoryEntity topCategory) {
		this.isbn = book.getIsbn();
		this.category = new CategoryDto(book.getCategory());
		this.topCategory = new CategoryDto(topCategory);
		this.title = book.getTitle();
		this.imgUrl = book.getImgUrl();
		this.author = book.getAuthor();
		this.price = book.getPrice();
		this.publisher = book.getPublisher();
		this.pubDate = book.getPubDate();
		this.description = book.getDescription();
		this.contents = book.getContents();
		this.author_intro = book.getAuthor_intro();
		this.stock = book.getStock();
	}
	
}
