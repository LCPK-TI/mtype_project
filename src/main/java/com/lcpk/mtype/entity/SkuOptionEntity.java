package com.lcpk.mtype.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SKU_OPTION_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SkuOptionId.class) // 복합키 클래스 지정
public class SkuOptionEntity {
	@Id // 복합키 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SKU_NO")
    private ProductSkuEntity productSku;
    
    @Id // 복합키 2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_NO")
    private OptionEntity option;
}
