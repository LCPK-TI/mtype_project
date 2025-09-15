package com.lcpk.mtype.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryInfo {
    private final CategoryDto topCategory;
    private final List<CategoryDto> midCategories;
    private final List<CategoryDto> subCategories;
    private final CategoryDto clickedCategory;
}