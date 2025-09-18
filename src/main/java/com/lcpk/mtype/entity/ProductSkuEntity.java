package com.lcpk.mtype.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
@Table(name="PRODUCT_SKU_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="productSku_seq_gen")
	@SequenceGenerator(
			name="productSku_seq_gen",
			sequenceName="PRODUCT_SKU_SQ",
			allocationSize=1)
	@Column(name="SKU_NO")
	private Long skuNo;

    @Column(name = "STOCK", nullable = false)
    private int stock;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_NO", nullable = false)
    private ProductEntity product;

    @OneToMany(mappedBy = "productSku", cascade = CascadeType.ALL)
    private List<SkuOptionEntity> skuOptions = new ArrayList<>();
}
