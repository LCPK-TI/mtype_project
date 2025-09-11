package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long>{

	//PARENT_CATEGORY_NO가 parentCategoryNo인 카테고리들만 리스트로 반환
	List<CategoryEntity> findByParentCategoryNo(Long parentCategoryNo);
	
	
	CategoryEntity findByParentCategoryNoAndDepth(Long parentCategoryNo, int depth);
}
