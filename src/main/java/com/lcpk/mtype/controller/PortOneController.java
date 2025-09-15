package com.lcpk.mtype.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PortOneController {

    @Value("${portone.api.secret}")
    private String portoneApiSecret;

    @PostMapping("/identity-verifications")
    public ResponseEntity<?> getIdentityVerificationResult(@RequestBody Map<String, String> body) {
        String identityVerificationId = body.get("identityVerificationId");
        if (identityVerificationId == null || identityVerificationId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "identityVerificationId is required"));
        }

        // REST API로 본인인증 정보 조회
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneApiSecret);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "https://api.portone.io/identity-verifications/" + URLEncoder.encode(identityVerificationId, StandardCharsets.UTF_8);

        ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.GET, entity, Map.class);

        if (!resp.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "PortOne verification API error", "details", resp.getBody()));
        }

        Map<String, Object> respBody = resp.getBody();
        // PortOne v2 문서에서는 조회 결과가 JSON 형태로 오고, "status" 필드가 중요한 키임
        String status = (String) respBody.get("status");
        Map<String, Object> verifiedCustomer = (Map<String, Object>) respBody.get("verifiedCustomer");

        Map<String, Object> result = Map.of(
            "status", status,
            "verifiedCustomer", verifiedCustomer
        );

        return ResponseEntity.ok(result);
    }
}