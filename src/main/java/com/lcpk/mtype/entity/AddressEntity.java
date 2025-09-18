package com.lcpk.mtype.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ADDR_LIST_TB")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Oracle 12c+ IDENTITY 사용
    @Column(name = "ADDR_NO")
    private Long addrNo;

    @Column(name = "USER_NO", nullable = false)
    private Long userNo;

    @Column(name = "RECIPIENT", nullable = false, length = 50)
    private String recipient;

    @Column(name = "RECI_PHONE", length = 30)
    private String reciPhone;

    @Column(name = "ADDR_MAIN", nullable = false, length = 200)
    private String addrMain;

    @Column(name = "ADDR_DETAIL", length = 200)
    private String addrDetail;

    @Column(name = "ADDR_DEFAULT", nullable = false)
    private boolean addrDefault;   // NUMBER(1) ↔ boolean 자동 매핑

    @Column(name = "ADDR_NAME", nullable = false, length = 50)
    private String addrName;
}
