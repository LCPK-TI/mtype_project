package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	// 부모 no로 자식 카테고리 목록을 조회
    @Query("SELECT c FROM CategoryEntity c WHERE c.parent.categoryNo = :parentNo")
    List<CategoryEntity> findByParentNo(@Param("parentNo") Long parentNo);
}
