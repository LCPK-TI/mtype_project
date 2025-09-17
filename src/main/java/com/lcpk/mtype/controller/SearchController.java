package com.lcpk.mtype.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcpk.mtype.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

	private final SearchService searchService;
	
	// 사용자가 검색 시 호출
	// 최근 검색어 추가 및 인기 검색어 점수 증가 처리.
	@PostMapping
	public ResponseEntity<Void> performSearch(@CookieValue(name = "jwtToken", required = false) String token, @RequestBody Map<String, String> payload){
		String keyword = payload.get("keyword");
		// 검색어가 존재하는 경우에 진행
		if(keyword != null && !keyword.trim().isEmpty()) {
			searchService.addRecentSearch(token, keyword);	// 최근 검색어에 추가
			searchService.incrementPopularSearch(keyword);	// 인기 검색어에 검색어 점수 증가
		}
		return ResponseEntity.ok().build();
	}
	
	// 사용자 최근 검색어 목록 조회
	@GetMapping("/recent")
	public ResponseEntity<List<String>> getRecentSearches(@CookieValue(name = "jwtToken", required = false) String token){
		return ResponseEntity.ok(searchService.getRecentSearches(token));
	}
	
	// 최근 검색어 개별 삭제
	@DeleteMapping("/recent")
	public ResponseEntity<Void> deleteRecentSearch(@CookieValue(name="jwtToken", required = false) String token, @RequestParam("keyword") String keyword){
		searchService.deleteRecentSearch(token, keyword);
		return ResponseEntity.ok().build();
	}
	
	// 최근 검색어 전체 삭제
	@DeleteMapping("/recent/all")
	public ResponseEntity<Void> deleteAllRecentSearches(@CookieValue(name = "jwtToken", required = false) String token){
		searchService.deleteAllRecentSearches(token);
		return ResponseEntity.ok().build();
	}
	
	// 인기 검색어 조회
	@GetMapping("/popular")
	public ResponseEntity<Set<String>> getPopularSearches(){
		return ResponseEntity.ok(searchService.getPopularSearches());
	}
}
