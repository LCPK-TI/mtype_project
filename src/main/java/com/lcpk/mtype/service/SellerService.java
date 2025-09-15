package com.lcpk.mtype.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.entity.SellerEntity;
import com.lcpk.mtype.repository.SellerRepository;
import com.lcpk.mtype.security.JwtUtil;

@Service
public class SellerService {
	
	private final SellerRepository sellerRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	@Autowired
	public SellerService(SellerRepository sellerRepository, JwtUtil jwtUtil) {
		this.sellerRepository = sellerRepository;
		this.passwordEncoder = new BCryptPasswordEncoder(); //직접 생성
		this.jwtUtil = jwtUtil;
	}
	
	//회원가입 시 암호화
	public SellerEntity registerSeller(SellerEntity seller) {
		seller.setSellerPw(passwordEncoder.encode(seller.getSellerPw())); //암호화
		seller.setJoinedDate(LocalDate.now());
		return sellerRepository.save(seller);
	}

	//비밀번호 유효성 검사
	public boolean isValidSellerPw(String sellerPw) {
		String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,12}$";
		return Pattern.matches(regex, sellerPw);
	}
	
	//아이디 중복 여부 판단
	public boolean isDuplicateSellerId(String sellerId) {
		return sellerRepository.findBySellerId(sellerId).isPresent();
	}
	
	//로그인 시 암호화된 비밀번호 비교
	public boolean login(String sellerId, String rawPassword) {
		Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
		if(sellerOpt.isPresent()) {
			String encodedPw = sellerOpt.get().getSellerPw();
			return passwordEncoder.matches(rawPassword, encodedPw);
		}
		return false;
	}
	
	//JWT 토큰 발급하는 로그인 처리
	public Map<String, String> loginAndGenerateTokens(String sellerId, String rawPassword) {
	    Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
	    
	    if (sellerOpt.isPresent()) {
	        SellerEntity seller = sellerOpt.get();
	        if (passwordEncoder.matches(rawPassword, seller.getSellerPw())) {
	            
	            String accessToken = jwtUtil.createAccessToken(sellerId);
	            String refreshToken = jwtUtil.createRefreshToken(sellerId);

	            // 추후 Redis 또는 DB에 RefreshToken 저장 가능 (여기선 생략)

	            Map<String, String> tokens = new HashMap<>();
	            tokens.put("accessToken", accessToken);
	            tokens.put("refreshToken", refreshToken);
	            return tokens;
	        }
	    }
	    return null;
	}
	
	// sellerId를 기반으로 판매자 정보를 조회
	public Optional<SellerEntity> findBySellerId(String sellerId) {
	    return sellerRepository.findBySellerId(sellerId);
	}

	// 판매자 정보를 저장
	public SellerEntity save(SellerEntity seller) {
	    return sellerRepository.save(seller);
	}
	
	// Refresh Token Update
	public void updateRefreshToken(String sellerId, String refreshToken) {
	    Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
	    if (sellerOpt.isPresent()) {
	        SellerEntity seller = sellerOpt.get();
	        seller.setRefreshToken(refreshToken);
	        sellerRepository.save(seller);
	    }
	}
	
	// 로그아웃
	public void clearRefreshToken(String sellerId) {
	    Optional<SellerEntity> sellerOpt = sellerRepository.findBySellerId(sellerId);
	    if (sellerOpt.isPresent()) {
	        SellerEntity seller = sellerOpt.get();
	        seller.setRefreshToken(null);
	        sellerRepository.save(seller);
	    }
	}
		
}
