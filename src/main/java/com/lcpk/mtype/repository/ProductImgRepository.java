package com.lcpk.mtype.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.ProductImgEntity;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImgEntity, Long>{
	
	//여러 카테고리 번호에 해당하는 상품 조회
	List<ProductImgEntity> findByProduct_ProductNo(Long productNo);
	
	//대표이미지 가져오기
	Optional<ProductImgEntity> findByProduct_ProductNoAndIsMain(Long productNo,String isMain);
}
