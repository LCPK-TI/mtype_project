package com.lcpk.mtype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PRODUCT_CATEGORY_TB")
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
	
	@Column(name="PARENT_CATEGORY_NO")
	private Long parentCategoryNo; //NULL 인경우가 있기 때문에 필드 Integer로 지정
	
	@Column(name="MBTI_NM")
	private String mbtiName;

	@Column(name="DEPTH")
	private int depth;
	public int getDepth() {
		return depth;
	}
	public Long getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(Long categoryNo) {
		this.categoryNo = categoryNo;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getParentCategoryNo() {
		return parentCategoryNo;
	}
	public void setParentCategoryNo(Long parentCategoryNo) {
		this.parentCategoryNo = parentCategoryNo;
	}
	public String getMbtiName() {
		return mbtiName;
	}
	public void setMbtiName(String mbtiName) {
		this.mbtiName = mbtiName;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
	