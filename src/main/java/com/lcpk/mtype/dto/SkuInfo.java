package com.lcpk.mtype.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SkuInfo {
	private Long skuNo;
	private int stock;
	private List<Long> optionNumbers;
}
