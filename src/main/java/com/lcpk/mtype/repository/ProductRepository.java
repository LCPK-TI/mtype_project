package com.lcpk.mtype.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	// Product, Seller, ProductImage를 조인하고,
	// isMain = 'Y'인 대표 이미지만 가져와서 ProductListDto로 바로 생성
	@Query("SELECT new com.lcpk.mtype.dto.ProductListDto(p.productNo, p.productName, p.productPrice, img.imgUrl) "
			+ "FROM ProductEntity p " + "LEFT JOIN p.images img ON img.isMain = 'Y' ")
	Slice<ProductListDto> findAllProjectedBy(Pageable pageable);

	@Query("SELECT new com.lcpk.mtype.dto.ProductListDto(p.productNo, p.productName, p.productPrice, img.imgUrl) "
			+ "FROM ProductEntity p " + "LEFT JOIN p.images img ON img.isMain = 'Y' "
			+ "WHERE p.category.categoryNo IN :categoryNos") // <-- WHERE 절이 IN으로 변경됨
	Slice<ProductListDto> findByCategoryNosIn(@Param("categoryNos") List<Long> categoryNos, Pageable pageable);

	// 상품 번호로 상품의 모든 연관 데이터(카테고리, 이미지)를 한 번에 조회
	@Query("SELECT p FROM ProductEntity p " + "JOIN FETCH p.category " + "LEFT JOIN FETCH p.images "
			+ "WHERE p.productNo = :productNo")
	Optional<ProductEntity> findProductWithDetailsByProductNo(@Param("productNo") Long productNo);

	// 상품명(productName)에 키워드가 포함된 상품을 검색
	@Query("SELECT new com.lcpk.mtype.dto.ProductListDto(p.productNo, p.productName, p.productPrice, img.imgUrl) "
			+ "FROM ProductEntity p LEFT JOIN p.images img ON img.isMain = 'Y' "
			+ "WHERE p.productName LIKE %:query%")
	Slice<ProductListDto> findByProductNameContaining(@Param("query") String query, Pageable pageable);
}
