package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
	
	//여러 카테고리 번호에 해당하는 상품 조회
	List<ProductEntity> findByCategoryNoIn(List<Long> categoryNos);
}
