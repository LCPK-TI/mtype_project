package com.lcpk.mtype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResponse {
    private String name;
    private String birthday;
    private String phone;
}
