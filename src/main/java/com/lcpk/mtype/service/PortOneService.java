package com.lcpk.mtype.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcpk.mtype.dto.VerifyResponse;

@Service
public class PortOneService {

    @Value("${portone.api-secret}")
    private String portoneApiSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VerifyResponse getIdentityVerificationInfo(String identityVerificationId) {
        
        String url = "https://api.portone.io/identity-verifications/" + identityVerificationId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneApiSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            JsonNode root = objectMapper.readTree(response.getBody());
            
            String status = root.path("status").asText();

            if ("VERIFIED".equals(status)) {
                JsonNode data = root.path("verifiedCustomer");
                String name = data.path("name").asText();
                String birthday = data.path("birthDate").asText(); // birthDate 필드 사용
                String phone = data.path("phoneNumber").asText(); // phoneNumber 필드 사용
                return new VerifyResponse(name, birthday, phone);
            } else {
                String reason = root.path("reason").asText("알 수 없는 이유로 인증 실패");
                throw new RuntimeException("PortOne 인증 실패: " + reason);
            }

        } catch (Exception e) {
            throw new RuntimeException("PortOne API 호출 중 오류가 발생했습니다.", e);
        }
    }
}