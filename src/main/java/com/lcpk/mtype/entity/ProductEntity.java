package com.lcpk.mtype.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PRODUCT_TB")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="product_seq_gen")
	@SequenceGenerator(
			name="product_seq_gen",
			sequenceName="PRODUCT_SQ",
			allocationSize=1)
	@Column(name="PRODUCT_NO")
	private Long productNo;
	
	@Column(name="CATEGORY_NO")
	private Long categoryNo;
	
	@Column(name="SELLER_NO")
	private int sellerNo;
	
	@Column(name="PRODUCT_NM")
	private String productName;
	
	@Column(name="PRODUCT_PRICE")
	private int productPrice;
	
//	@Column(name="PRODUCT_STOCK")
//	private int productStock;
	
	@Column(name="DETAIL_IMG_URL")
	private String detailImgUrl;

	//상품이미지
	@OneToMany(mappedBy="product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProductImgEntity> images;
	
	// 썸네일만 가져오기
    public String getThumbnailUrl() {
        if (images == null) return null;
        return images.stream()
                .filter(img -> "Y".equals(img.getIsMain()))
                .map(ProductImgEntity::getImgUrl)
                .findFirst()
                .orElse(null);
    }
	public Long getProductNo() {
		return productNo;
	}

	public void setProductNo(Long productNo) {
		this.productNo = productNo;
	}

	public Long getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(Long categoryNo) {
		this.categoryNo = categoryNo;
	}

	public int getSellerNo() {
		return sellerNo;
	}

	public void setSellerNo(int sellerNo) {
		this.sellerNo = sellerNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

//	public int getProductStock() {
//		return productStock;
//	}
//
//	public void setProductStock(int productStock) {
//		this.productStock = productStock;
//	}

	public String getDetailImgUrl() {
		return detailImgUrl;
	}

	public void setDetailImgUrl(String detailImgUrl) {
		this.detailImgUrl = detailImgUrl;
	}
}
