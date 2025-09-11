package com.lcpk.mtype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcpk.mtype.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByKakaoUserId(Long kakaoUserId);	// 카카오 아이디로 사용자 찾기
}
