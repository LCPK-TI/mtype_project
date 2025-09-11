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
	
	public List<CategoryEntity> findMbtiCategories(String mbti){
		return categoryRepository.findByMbtiName(mbti);
	}
	
	//mbti 해당하는 카테고리 + 부모 재귀로 조회
	public List<CategoryEntity> findMbtiCategoryPath(String mbti) {
        List<CategoryEntity> mbtiCategories = categoryRepository.findByMbtiName(mbti);
        List<CategoryEntity> result = new ArrayList<>();

        for (CategoryEntity cat : mbtiCategories) {
            addCategoryWithParents(cat, result);
        }
        return result;
    }
	private void addCategoryWithParents(CategoryEntity category, List<CategoryEntity> result) {
        if (!result.contains(category)) {
            result.add(category);
        }
        if (category.getParentCategoryNo() != null) {
            CategoryEntity parent = categoryRepository.findById(category.getParentCategoryNo()).orElse(null);
            if (parent != null) {
                addCategoryWithParents(parent, result);
            }
        }
    }

}
