package com.lcpk.mtype.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lcpk.mtype.dto.CategoryDto;
import com.lcpk.mtype.dto.CategoryInfo;
import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	//모든 페이지의 헤더에 공통으로 사용될 전체 계층형 카테고리 메뉴 데이터 생성
	public List<CategoryDto> getCategoryMenu() {
		//모든 카테고리 조회
		List<CategoryEntity> allCategories = categoryRepository.findAll();

		//DTO 리스트로 변환하고 Map에 담아 빠른 조회를 준비
		Map<Long, CategoryDto> categoryMap = allCategories.stream().map(CategoryDto::new)
				.collect(Collectors.toMap(CategoryDto::getCategoryNo, dto -> dto));

		//최상위 카테고리와 자식 카테고리를 연결
		List<CategoryDto> topLevelCategories = new ArrayList<>();
		allCategories.forEach(category -> {
			CategoryDto dto = categoryMap.get(category.getCategoryNo());
			if (category.getParent() == null) {
				// 부모가 없으면 최상위 카테고리 리스트에 추가
				topLevelCategories.add(dto);
			} else {
				// 부모가 있으면 부모의 자식 리스트에 추가
				Long parentId = category.getParent().getCategoryNo();
				categoryMap.get(parentId).getChildren().add(dto);
			}
		});

		return topLevelCategories;
	}
	//상품 목록 페이지의 카테고리 ui를 구성하는데 필요한 카테고리 정보
	public CategoryInfo getCategoryInfo(Long categoryNo) { //categoryNo : 현재 클릭된 카테고리의 no
		CategoryEntity clicked = categoryRepository.findById(categoryNo)
				.orElseThrow(() -> new IllegalArgumentException("Invalid category No:" + categoryNo));

		CategoryEntity topCategory;
		List<CategoryEntity> midCategories;
		List<CategoryEntity> subCategories;

		switch (clicked.getDepth()) {
		case 1: // 최상위 카테고리 클릭 시
			topCategory = clicked;
			midCategories = clicked.getChildren();
			subCategories = new ArrayList<>(); // 하위 카테고리는 없음
			break;
		case 2: // 중위 카테고리 클릭 시
			topCategory = clicked.getParent();
			midCategories = topCategory.getChildren();
			subCategories = clicked.getChildren();
			break;
		default: // 하위(3레벨) 카테고리 클릭 시
			CategoryEntity midCategory = clicked.getParent();
			topCategory = midCategory.getParent();
			midCategories = topCategory.getChildren();
			subCategories = midCategory.getChildren();
			break;
		}

		// 조회한 Entity들을 DTO로 변환하여 반환 (상위, 중위, 하위, 현재 카테고리 DTO 포함)
		return new CategoryInfo(new CategoryDto(topCategory),
				midCategories.stream().map(CategoryDto::new).collect(Collectors.toList()),
				subCategories.stream().map(CategoryDto::new).collect(Collectors.toList()), new CategoryDto(clicked));
	}

	//클릭한 카테고리의 최상위 카테고리
	public CategoryEntity findTopParent(Long categoryNo) {
		CategoryEntity category = categoryRepository.findById(categoryNo).orElseThrow(()->new RuntimeException("카테고리 없음"));
		if(category.getDepth()==1) {
			return category;
		}
		return findTopParent(category.getParent().getCategoryNo());
	}
	 // 클릭한 카테고리 포함 모든 하위 카테고리 ID 리스트 반환
    public List<Long> findAllSubCategoryIds(Long categoryNo) {
        List<Long> result = new ArrayList<>();
        result.add(categoryNo);
        collectChildren(categoryNo, result);
        return result;
    }

    //자식 카테고리 no를 수집
    private void collectChildren(Long parentNo, List<Long> result) {
    	//부모 id를 기준으로 직계 자식들을 찾음
        List<CategoryEntity> children = categoryRepository.findByParentNo(parentNo);
        for (CategoryEntity child : children) {
            result.add(child.getCategoryNo());
            //찾은 자식을 부모로 하여 재귀 호출
            collectChildren(child.getCategoryNo(), result);
        }
    }
//
//	 // MBTI 컬럼 값 가지는 카테고리 + Product + ProductImg 한 번에 조회
//    public List<CategoryEntity> getCategoryWithProductsByMbti(List<String> mbtiList) {
//        return categoryRepository.findCategoryWithProductsAndImagesByMbti(mbtiList);
//    }

}
