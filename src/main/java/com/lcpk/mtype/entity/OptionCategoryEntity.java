package com.lcpk.mtype.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OPTION_CATEGORY_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionCategoryEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_category_sq_generator")
    @SequenceGenerator(name = "option_category_sq_generator", sequenceName = "OPTION_CATEGORY_SQ", allocationSize = 1)
    @Column(name = "OPTION_CATEGORY_NO")
    private Long optionCategoryNo;

    @Column(name = "OPTION_CATEGORY_NM")
    private String optionCategoryNm;

    // OptionCategory(1) Option(N)
    // 카테고리가 삭제되면 하위 옵션들도 함께 삭제되도록 cascade 설정
    @OneToMany(mappedBy = "optionCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> options = new ArrayList<>();
}
