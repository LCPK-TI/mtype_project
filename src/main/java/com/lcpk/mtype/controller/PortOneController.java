/*package com.lcpk.mtype.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcpk.mtype.dto.VerifyRequest;
import com.lcpk.mtype.dto.VerifyResponse;
import com.lcpk.mtype.service.PortOneService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class PortOneController {

    private final PortOneService portOneService;


    @PostMapping("/complete")
    public VerifyResponse complete(@RequestBody VerifyRequest request) {
        return portOneService.getIdentityVerificationInfo(request.getIdentityVerificationId());
    }
}
*/