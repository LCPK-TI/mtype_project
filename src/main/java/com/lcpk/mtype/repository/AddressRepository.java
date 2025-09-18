package com.lcpk.mtype.repository;

import com.lcpk.mtype.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserNoOrderByAddrDefaultDescAddrNoDesc(Long userNo);
    List<AddressEntity> findByUserNoAndAddrDefaultTrue(Long userNo);
}
