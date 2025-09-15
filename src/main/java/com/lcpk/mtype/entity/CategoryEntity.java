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
@Table(name="PRODUCT_CATEGORY_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="category_seq_gen")
	@SequenceGenerator(
			name="category_seq_gen",
			sequenceName="PRODUCT_CATEGORY_SQ",
			allocationSize=1)
	@Column(name="CATEGORY_NO")
	private Long categoryNo;
	
	@Column(name="CATEGORY_NM")
	private String categoryName;
	
	
	@Column(name="MBTI_NM")
	private String mbtiName;

	@Column(name="DEPTH")
	private int depth;
	
	@OneToMany(mappedBy ="category",fetch=FetchType.LAZY)
	private List<ProductEntity> products;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CATEGORY_NO")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent")
    private List<CategoryEntity> children = new ArrayList<>();

	
}
	