package com.lcpk.mtype.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lcpk.mtype.config.jwt.JwtTokenProvider;
import com.lcpk.mtype.dto.VerifyResponse;
import com.lcpk.mtype.entity.User;
import com.lcpk.mtype.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PortOneService portOneService;
	
	 private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
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
	
	// 본인인증 정보로 DB의 사용자 정보 update
	public void updateUserWithVerification(String token, String identityVerificationId) {
		Long kakaoUserId = jwtTokenProvider.getKakaoUserIdFromToken(token);
		User user = userRepository.findByKakaoUserId(kakaoUserId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		
		// 포트원 서버에서 인증 정보 가져옴
		VerifyResponse verificationInfo = portOneService.getIdentityVerificationInfo(identityVerificationId);
		
		log.info("===== 포트원 본인인증 정보 확인 =====");
        log.info("인증된 사용자 이름: {}", verificationInfo.getName());
        log.info("인증된 생년월일: {}", verificationInfo.getBirthday());
        log.info("인증된 연락처: {}", verificationInfo.getPhone());
        log.info("====================================");
		
		// 받아온 정보로 사용자 정보 갱신
		user.setUserName(verificationInfo.getName());
		user.setUserPhone(verificationInfo.getPhone());
		//생년월일
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date birthDate = formatter.parse(verificationInfo.getBirthday());
			user.setUserBirth(birthDate);
		} catch (ParseException e) {
			throw new RuntimeException("생년월일 형식 변환 실패", e);
		}
		
		userRepository.save(user);
	}
}
