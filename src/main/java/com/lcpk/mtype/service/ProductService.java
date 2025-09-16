package com.lcpk.mtype.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.dto.OptionGroup;
import com.lcpk.mtype.dto.OptionInfo;
import com.lcpk.mtype.dto.ProductDetailDto;
import com.lcpk.mtype.dto.ProductListDto;
import com.lcpk.mtype.dto.SkuInfo;
import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.entity.OptionCategoryEntity;
import com.lcpk.mtype.entity.OptionEntity;
import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.ProductSkuEntity;
import com.lcpk.mtype.repository.ProductRepository;
import com.lcpk.mtype.repository.ProductSkuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductSkuRepository productSkuRepository;
	private final CategoryService categoryService;
	private static final int PAGE_SIZE = 20;

	public Slice<ProductListDto> getProductList(Long categoryNo, Pageable pageable) {
		if (categoryNo == null) {
			return productRepository.findAllProjectedBy(pageable);
		} else {
			// CategoryService를 통해 자신 및 모든 하위 카테고리 No 목록을 가져오기
			List<Long> categoryNos = categoryService.findAllSubCategoryIds(categoryNo);

			// no 목록을 Repository에 전달하여 상품 조회
			return productRepository.findByCategoryNosIn(categoryNos, pageable);
		}
	}

	public ProductDetailDto getProductDetail(Long productNo) {
		// 상품 기본 정보와 연관 엔티티들을 FetchJoin으로 한번에 조회
		ProductEntity product = productRepository.findProductWithDetailsByProductNo(productNo)
				.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + productNo));

		// 상품의 최상위 카테고리 조회
		CategoryEntity topCategory = categoryService.findTopParent(product.getCategory().getCategoryNo());

		// 상품에 연결된 모든 SKU와 하위 옵션 정보들을 한번에 조회
		List<ProductSkuEntity> skus = productSkuRepository.findAllWithDetailsByProductNo(productNo);

		// 비즈니스 로직을 통해 DTO 가공
		List<SkuInfo> skuInfos = createSkuInfos(skus);
		List<OptionGroup> optionGroups = createOptionGroups(skus);

		return ProductDetailDto.from(product, topCategory, optionGroups, skuInfos);
	}

	private List<SkuInfo> createSkuInfos(List<ProductSkuEntity> skus) {
		return skus.stream()
				.map(sku -> new SkuInfo(sku.getSkuNo(), sku.getStock(),  sku.getSkuOptions()
						.stream().map(so -> so.getOption().getOptionNo()).sorted().collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	
	//SKU 엔티티 리스트를 OptionGroup DTO 리스트로 변환하여 화면에 뿌려줄 옵션 선택창의 구조
	private List<OptionGroup> createOptionGroups(List<ProductSkuEntity> skus) {
		Map<String, Set<OptionInfo>> categoryMap = new LinkedHashMap<>();
		skus.forEach(sku -> {
			sku.getSkuOptions().forEach(skuOption -> {
				OptionEntity option = skuOption.getOption();
				// 기본 옵션은 화면에 노출할 필요가 없으므로 건너뜀
				if ("DEFAULT".equals(option.getOptionCode()))
					return;

				OptionCategoryEntity category = option.getOptionCategory();
				categoryMap
						.computeIfAbsent(category.getOptionCategoryNm(),
								k -> new TreeSet<>(Comparator.comparing(OptionInfo::getOptionNo)))
						.add(new OptionInfo(option.getOptionNo(), option.getOptionNm()));
			});
		});

		return categoryMap.entrySet().stream()
				.map(entry -> new OptionGroup(entry.getKey(), new ArrayList<>(entry.getValue())))
				.collect(Collectors.toList());
	}
}
