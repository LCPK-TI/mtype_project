package com.lcpk.mtype.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionGroup {
	private String categoryName;
	private List<OptionInfo> options;
}