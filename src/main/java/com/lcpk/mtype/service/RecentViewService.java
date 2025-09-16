package com.lcpk.mtype.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lcpk.mtype.config.jwt.JwtTokenProvider;
import com.lcpk.mtype.dto.RecentViewDto;
import com.lcpk.mtype.entity.ProductEntity;
import com.lcpk.mtype.entity.RecentViewEntity;
import com.lcpk.mtype.entity.User;
import com.lcpk.mtype.repository.ProductRepository;
import com.lcpk.mtype.repository.RecentViewRepository;
import com.lcpk.mtype.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentViewService {
	
	private final RecentViewRepository recentViewRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final JwtTokenProvider jwtTokenProvider;
	
	// 최대 저장 가능 개수
	private static final int MAX_RECENT_VIEWS = 20;
	
	// 트랜잭션 처리. 하나라도 실패하면 취소.
	@Transactional
	public void addRecentView(String token, Long productNo) {
		// jwt 토큰 해독 => 사용자의 카카오 번호 알아냄
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		
		// 카카오 번호로 회원 정보 조회.
		User user = userRepository.findByKakaoUserId(kakaoUserId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		
		// 상품 번호로 상품 정보를 찾음.
		ProductEntity productEntity = productRepository.findById(productNo)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
		
		// 최근 본 상품 테이블에 이미 같은 상품이 존재하는지 확인
		Optional<RecentViewEntity> existingView = recentViewRepository.findByUserAndProductEntity(user, productEntity);
		if(existingView.isPresent()) {
			// 테이블에 이미 존재하면, 조회 일자를 갱신
			existingView.get().updateViewDate();
			return;
		}
		// 최근 조회한 적이 없다면,
		// 사용자의 최근 본 상품이 몇개인지 확인.
		long currentCount = recentViewRepository.countByUser(user);
		
		// 조회 기록이 20개 이상인 경우 가장 오래된 상품 삭제
		if(currentCount >= MAX_RECENT_VIEWS) {
			recentViewRepository.findTopByUserOrderByViewDateAsc(user)
					.ifPresent(oldestView -> recentViewRepository.delete(oldestView));
		}
		// 새로 조회한 상품 insert
		RecentViewEntity newView = RecentViewEntity.builder()
				.user(user)
				.productEntity(productEntity)
				.build();
		recentViewRepository.save(newView);
	}
	
	// 사용자 최근 본 상품 목록 조회
	public List<RecentViewDto> getRecentViews(String token) {
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		User user = userRepository.findByKakaoUserId(kakaoUserId).orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다."));
		
		List<RecentViewEntity> recentViews = recentViewRepository.findByUserOrderByViewDateDesc(user);
		
		return recentViews.stream()
				.map(RecentViewDto::new)
				.collect(Collectors.toList());
	}
	
	// 최근 본 상품 미리보기 (4개)
	public List<RecentViewDto> getRecentViewPreview(String token){
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		User user = userRepository.findByKakaoUserId(kakaoUserId)
				.orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다."));
		
		List<RecentViewEntity> recentViews = recentViewRepository.findTop4ByUserOrderByViewDateDesc(user);
		
		return recentViews.stream()
				.map(RecentViewDto::new)
				.collect(Collectors.toList());
	}
}
