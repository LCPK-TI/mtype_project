package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lcpk.mtype.entity.ProductSkuEntity;

public interface ProductSkuRepository extends JpaRepository<ProductSkuEntity, Long> {
    
    // JPQL을 사용해 연관된 엔티티들을 한번에 JOIN FETCH하여 조회
    @Query("SELECT ps FROM ProductSkuEntity ps " +
           "JOIN FETCH ps.skuOptions so " +
           "JOIN FETCH so.option o " +
           "JOIN FETCH o.optionCategory " +
           "WHERE ps.product.productNo = :productNo")
    List<ProductSkuEntity> findAllWithDetailsByProductNo(@Param("productNo") Long productNo);
}