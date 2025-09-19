package com.lcpk.mtype.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lcpk.mtype.entity.SellerEntity;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
	
	//판매자 정보 조회
	Optional<SellerEntity> findBySellerId(String sellerId);

}
