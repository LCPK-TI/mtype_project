package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lcpk.mtype.entity.ProductSkuEntity;

public interface ProductSkuRepository extends JpaRepository<ProductSkuEntity, Long> {
    @Query("SELECT ps FROM ProductSkuEntity ps " +
            "LEFT JOIN FETCH ps.skuOptions so " +
            "LEFT JOIN FETCH so.option o " +
            "LEFT JOIN FETCH o.optionCategory " +
            "WHERE ps.product.productNo = :productNo")
     List<ProductSkuEntity> findAllWithDetailsByProductNo(@Param("productNo") Long productNo);
 }