package com.lcpk.mtype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcpk.mtype.entity.SkuOptionEntity;
import com.lcpk.mtype.entity.SkuOptionNo;


public interface SkuOptionRepository extends JpaRepository<SkuOptionEntity, SkuOptionNo> {}
