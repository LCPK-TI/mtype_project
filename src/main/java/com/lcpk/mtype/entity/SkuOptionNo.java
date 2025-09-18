package com.lcpk.mtype.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuOptionNo implements Serializable{
	private Long productSku;
    private Long option;
}
