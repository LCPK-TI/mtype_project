package com.lcpk.mtype.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="PRODUCT_TB")
@Getter
@Setter
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
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_NO")
    private CategoryEntity category;

    // Seller(스토어)와의 관계 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SELLER_NO")
//    private Seller seller;
	
	@Column(name="PRODUCT_NM")
	private String productName;
	
	@Column(name="PRODUCT_PRICE")
	private int productPrice;
	
	@Column(name="PRODUCT_STOCK")
	private int productStock;
	
	@Column(name="DETAIL_IMG_URL")
	private String detailImgUrl;

    // ProductImg와 OneToMany 관계(상품 하나에 여러 이미지)
    @OneToMany(mappedBy = "product")
    private List<ProductImgEntity> images = new ArrayList<>();
    
    //product(1) productOption(n)
    @OneToMany(mappedBy="product") //cascade = CascadeType.ALL, orphanRemoval = true
    private List<ProductOptionEntity> productOptions = new ArrayList<>();
}
