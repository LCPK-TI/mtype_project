package com.lcpk.mtype.exception;

import lombok.Getter;

// 탈퇴한 회원이 로그인 시도했을 시의 예외
@Getter
public class WithdrawnUserException extends RuntimeException {

	//예외 발생 시 카카오 ID를 전달.
	private final Long kakaoUserId;
	
	public WithdrawnUserException(String message, Long kakaoUserId) {
		super(message);
		this.kakaoUserId = kakaoUserId;
	}
}
