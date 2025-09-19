package com.lcpk.mtype.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOK_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
	@Id
	@Column(name = "ISBN")
	private Long isbn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_NO")
	private CategoryEntity category;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "IMG_URL")
	private String imgUrl;

	@Column(name = "AUTHOR")
	private String author;

	@Column(name = "PRICE")
	private Long price;

	@Column(name = "PUBLISHER")
	private String publisher;

	@Column(name = "PUB_DATE")
	private Date pubDate;

	@Lob
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Lob
	@Column(name = "CONTENTS")
	private String contents;
	
	@Lob
	@Column(name = "AUTHOR_INTRO")
	private String author_intro;
	
	@Column(name="STOCK")
	private Long stock;

}