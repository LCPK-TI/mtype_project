package com.lcpk.mtype.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.entity.ProductImgEntity;
import com.lcpk.mtype.repository.ProductImgRepository;

@Service
public class ProductImgService {

	@Autowired
	private ProductImgRepository repository;
	
	public List<ProductImgEntity> findByProduct_ProductNo(Long productNo) {
		return repository.findByProduct_ProductNo(productNo);
	}
	
}
