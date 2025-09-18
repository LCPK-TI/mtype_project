package com.lcpk.mtype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionInfo {
	private Long optionNo;
	private String optionName;

	@Override
	public int hashCode() {
		return optionNo.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.optionNo.equals(((OptionInfo) obj).optionNo);
	}
}
