package com.lcpk.mtype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PRODUCT_IMG_TB")
@NoArgsConstructor
@AllArgsConstructor
public class ProductImgEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_img_seq_gen")
	@SequenceGenerator(
			name="product_img_seq_gen",
			sequenceName="PRODUCT_IMG_SQ",
			allocationSize=1)
	@Column(name="IMG_NO")
	private Long imgNo;
	
	@Column(name="IMG_URL")
	private String imgUrl;
	
	@Column(name="IS_MAIN")
	private String isMain;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUCT_NO")
	private ProductEntity product;

	public Long getImgNo() {
		return imgNo;
	}

	public void setImgNo(Long imgNo) {
		this.imgNo = imgNo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}
	
}
