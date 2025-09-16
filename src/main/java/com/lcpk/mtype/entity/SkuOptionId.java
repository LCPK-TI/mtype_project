package com.lcpk.mtype.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuOptionId implements Serializable {
    private Long productSku; // SkuOptionEntity의 productSku
    private Long option;     // SkuOptionEntity의 option
}