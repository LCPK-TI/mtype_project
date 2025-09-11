package com.lcpk.mtype.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lcpk.mtype.config.jwt.JwtTokenProvider;
import com.lcpk.mtype.entity.User;
import com.lcpk.mtype.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Transactional
	public void withdrawUser(String token) {
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		User user = userRepository.findByKakaoUserId(kakaoUserId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		user.withdraw();
	}
	
	// 탈퇴 취소 및 jwt 토큰 발급
	@Transactional
	public String reactivateUserAndCreateToken(Long kakaoUserId) {
		User user = userRepository.findByKakaoUserId(kakaoUserId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		user.reactivate();
		return jwtTokenProvider.createToken(user.getKakaoUserId());
	}
}
