package com.lcpk.mtype.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private  CategoryRepository categoryRepository;
	
	//상위 카테고리 기준 본인 포함 모든 하위 카테고리 no 조회
	public List<Long> getAllChildCategoryNo(Long categoryNo){
		List<Long> childNos = new ArrayList<>();
		childNos.add(categoryNo); //클릭한 카테고리 번호를 포함한다.
		
		List<CategoryEntity> children = categoryRepository.findByParentCategoryNo(categoryNo);
		for(CategoryEntity c : children) {
			childNos.addAll(getAllChildCategoryNo(c.getCategoryNo())); //재귀 호출
		}
		return childNos;
	}
	
	public List<CategoryEntity> getAllCategories(){
		return categoryRepository.findAll();
	}
	//특정 카테고리 조회
//	public CategoryEntity findById(int categoryNo){
//		return categoryRepository.findById(categoryNo).orElse(null);
//	}
//	
	//클릭한 하위 카테고리 기준 최상위 카테고리 찾기
//	public CategoryEntity findTopParent(Long categoryNo) {
//		if(categoryNo==null) {
//			return findById(categoryNo); //최상위
//		}
//		return findTopParent(categoryRepository.findById(category.getParentCategoryNo()).orElse(null));
//	}
//	
//	//특정 최상위 카테고리의 하위 카테고리 리스트
//	public List<CategoryEntity> findChildren(int parentNo){
//		return categoryRepository.findByParentCategoryNo(parentNo);
//	}
	//클릭한 카테고리
	public CategoryEntity findById(Long categoryNo) {
		return categoryRepository.findById(categoryNo).orElse(null);
	}
	//클릭한 카테고리의 최상위 카테고리
	public CategoryEntity findTopParent(Long categoryNo) {
		CategoryEntity category = categoryRepository.findById(categoryNo).orElseThrow(()->new RuntimeException("카테고리 없음"));
		if(category.getDepth()==1) {
			return category;
		}
		return findTopParent(category.getParentCategoryNo());
	}
	//클릭한 카테고리의 직계 하위 카테고리 조회
	public List<CategoryEntity> findChildren(Long categoryNo){
		return categoryRepository.findByParentCategoryNo(categoryNo);
	}
}
