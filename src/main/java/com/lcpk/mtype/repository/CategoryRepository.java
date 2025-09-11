package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long>{

	//PARENT_CATEGORY_NO가 parentCategoryNo인 카테고리들만 리스트로 반환
	List<CategoryEntity> findByParentCategoryNo(Long parentCategoryNo);
	
	//선택한 mbti를 가진 최하위 카테고리 조회	depth3 또는 depth2 최하위 mbti 카테고리
	List<CategoryEntity> findByMbtiName(String mbti);
}
