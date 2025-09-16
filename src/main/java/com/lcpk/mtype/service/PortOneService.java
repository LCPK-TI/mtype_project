package com.lcpk.mtype.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.lcpk.mtype.dto.VerifyResponse;

@Service
public class PortOneService {
	@Value("${portone.api.key}")
	private String apiKey;

	@Value("${portone.api.secret}")
	private String apiSecret;

	// ✅ RestTemplate은 매번 생성하는 것보다 Bean으로 등록하거나 멤버 변수로 두는 것이 좋습니다.
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String BASE_URL = "https://api.iamport.io/v2";

	// AccessToken 발급
	private String getAccessToken() {
		String url = BASE_URL + "/users/token";

		Map<String, String> body = Map.of("apiKey", apiKey, "secret", apiSecret);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

		// ✅ V2 응답 구조에 맞게 수정하고, 불필요한 코드를 삭제했습니다.
		return response.getBody().get("accessToken").asText();
	}

	// 본인인증 결과 조회
	public VerifyResponse getIdentityVerificationInfo(String identityVerificationId) {
		String token = getAccessToken();

		String url = BASE_URL + "/identity-verifications/" + identityVerificationId;

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

		// ✅ V2 응답에서는 주요 정보가 'data' 객체 안에 있습니다.
		JsonNode data = response.getBody().get("data");

		String name = data.get("name").asText();
		String birthday = data.get("birth").asText(); // ✅ 필드 이름이 birth 입니다.
		String phone = data.get("phone").asText();

		return new VerifyResponse(name, birthday, phone);
	}
}