package com.lcpk.mtype.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.config.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final StringRedisTemplate redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;
	
	// Redis Key
	private static final String RECENT_SEARCH_KEY_PREFIX = "recent_searches:"; // 최근 검색
	private static final String POPULAR_SEARCH_KEY = "popular_searches:";	// 인기 검색
	private static final int MAX_SEARCH_COUNT = 5;	// 최대 저장 개수
	
	// 최근 검색어 목록에 검색어 추가
	public void addRecentSearch(String token, String keyword) {
		// 로그인하지 않았을 경우 검색어 저장 X
		if(token == null || token.isEmpty()) return;
		
		// 사용자 카카오 번호 추출 및 사용자 Key 생성
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		String userKey = RECENT_SEARCH_KEY_PREFIX + kakaoUserId;
		
		// Redis 리스트 왼쪽에 새로운 검색어 push
		redisTemplate.opsForList().leftPush(userKey, keyword);
		// 리스트 크기를 5로 제한. (6번째 이후 요소들 제거(TRIM))
		redisTemplate.opsForList().trim(userKey, 0, MAX_SEARCH_COUNT-1);
	}
	
	// 사용자 최근 검색어 내역 조회
	public List<String> getRecentSearches(String token){
		if(token == null || token.isEmpty()) {
			return Collections.emptyList(); // 로그인 아닐 경우 빈 리스트
		}
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		String userKey = RECENT_SEARCH_KEY_PREFIX + kakaoUserId;
		
		// Redis 리스트에서 5개 요소 조회(0~4)
		return redisTemplate.opsForList().range(userKey, 0, MAX_SEARCH_COUNT-1);
	}
	
	// 최근 검색어 개별 삭제
	public void deleteRecentSearch(String token, String keyword) {
		if(token == null && token.isEmpty()) return;
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		String userKey = RECENT_SEARCH_KEY_PREFIX + kakaoUserId;
		
		//Redis 리스트에서 특정 키워드 찾아 삭제 (0은 일치항목 모두 삭제)
		redisTemplate.opsForList().remove(userKey, 0, keyword);
	}
	
	// 최근 검색어 전체 삭제
	public void deleteAllRecentSearches(String token) {
		if(token == null && token.isEmpty()) return;
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		String userKey = RECENT_SEARCH_KEY_PREFIX + kakaoUserId;
		
		// 해당 사용자의 키 삭제
		redisTemplate.delete(userKey);
	}
	
	// 인기 검색어
	// 검색어 count  1씩 증가
	public void incrementPopularSearch(String keyword) {
		// Redis Sorted Set 점수 1 증가.
		// 처음 검색된 키워드일 경우 새로 만들고 1로 setting
		redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, keyword, 1);
	}
	
	// 인기 검색어 Top5 조회
	public Set<String> getPopularSearches(){
		// Redis Sorted Set에서 점수 높은 순으로 5개(0~4) 조회
		return redisTemplate.opsForZSet().reverseRange(POPULAR_SEARCH_KEY, 0, MAX_SEARCH_COUNT-1);
	}
}
