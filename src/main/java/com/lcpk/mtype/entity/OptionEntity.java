package com.lcpk.mtype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OPTION_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_sq_generator")
    @SequenceGenerator(name = "option_sq_generator", sequenceName = "OPTION_SQ", allocationSize = 1)
    @Column(name = "OPTION_NO")
    private Long optionNo;

    @Column(name = "OPTION_NM", length = 20)
    private String optionNm;

    // Option(N) OptionCategory(1)
    // 옵션은 반드시 카테고리가 있어야 하므로 nullable = false
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_CATEGORY_NO", nullable = false)
    private OptionCategoryEntity optionCategory;
}
