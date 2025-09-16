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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="PRODUCT_OPTION_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="productOption_seq_gen")
	@SequenceGenerator(
			name="productOption_seq_gen",
			sequenceName="PRODUCT_OPTION_SQ",
			allocationSize=1)
	@Column(name="PRODUCT_OPTION_NO")
	private Long productOptionNo;
	
	//productOption(N) productEntity(1)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUCT_NO" , nullable = false)
	private ProductEntity product; 
	
	// ProductOption(N) Option(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_NO", nullable = false)
    private OptionEntity option;
}
