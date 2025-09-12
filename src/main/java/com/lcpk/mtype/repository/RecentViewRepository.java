package com.lcpk.mtype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.RecentViewEntity;
import com.lcpk.mtype.entity.User;

@Repository
public interface RecentViewRepository extends JpaRepository<RecentViewEntity, Long>{
	// 사용자가 해당 상품을 본적 있는지 조회하기 위함
	Optional<RecentViewEntity> findByUserAndProduct(User user, ProductEntity product);
	
	// 사용자의 최근 본 상품 개수
	long countByUser(User user);
	
	// 사용자의 최근 본 상품 기록 중 가장 오래된 상품 찾기 위함.
	Optional<RecentViewEntity> findTopByUserOrderByViewDateAsc(User user);
}
