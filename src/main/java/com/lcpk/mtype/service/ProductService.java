package com.lcpk.mtype.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryService categoryService;

	//상위 카테고리 클릭 시 하위 카테고리 상품 모두 조회
	public List<ProductEntity> getProductsByCategory(Long categoryNo) {
		List<Long> categoryNos = categoryService.getAllChildCategoryNo(categoryNo);
		return repository.findByCategoryNoIn(categoryNos);
	}
	
	public Optional<ProductEntity> findById(Long categoryNo) {
		return repository.findById(categoryNo);
	}
}
