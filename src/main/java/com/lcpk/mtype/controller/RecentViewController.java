package com.lcpk.mtype.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcpk.mtype.service.RecentViewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recent-views")
public class RecentViewController {
	private final RecentViewService recentViewService;
	
	@PostMapping("/{productNo}")
	public ResponseEntity<Void> addRecentView(@RequestHeader("Authorization") String accessToken, @PathVariable("productNo") Long productNo){
		String token = accessToken.substring(7);
		recentViewService.addRecentView(token, productNo);
		
		return ResponseEntity.ok().build();
	}
}
