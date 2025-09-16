package com.lcpk.mtype.dto;

import java.util.ArrayList;
import java.util.List;

import com.lcpk.mtype.entity.CategoryEntity;

import lombok.Getter;

@Getter
public class CategoryDto {
    private final Long categoryNo;
    private final String categoryName;
    private final Long parentCategoryNo;
    private final int depth;
    private List<CategoryDto> children = new ArrayList<>(); //자식 카테고리를 담을 리스트

    public CategoryDto(CategoryEntity category) {
        this.categoryNo = category.getCategoryNo();
        this.categoryName = category.getCategoryName();
        this.parentCategoryNo = (category.getParent() != null) ? category.getParent().getCategoryNo() : null;
        this.depth = category.getDepth();
    }
}
