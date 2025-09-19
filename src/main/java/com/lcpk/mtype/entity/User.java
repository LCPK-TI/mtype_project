package com.lcpk.mtype.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER_TB")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sq_generator")
	@SequenceGenerator(
		name = "user_sq_generator",
		sequenceName = "USER_SQ",
		allocationSize = 1
	)
	@Column(name = "USER_NO")
	private long userNo;
	
	@Column(name = "KAKAO_USER_ID", nullable = false, unique = true)
	private long kakaoUserId;
	
	@Column(name = "USER_EMAIL", nullable = false, unique = true)
	private String userEmail;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "USER_ADDR")
	private String userAddr;
	
	@Column(name = "USER_PHONE")
	private String userPhone;
	
	@Column(name = "USER_BIRTH")
	@Temporal(TemporalType.DATE)
	private Date userBirth;
	
	@Column(name = "USER_MBTI")
	private String userMbti;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "USER_IS_WITHDRAWN", nullable = false, length = 1)
	@Builder.Default
	private YesOrNo userIsWithdrawn = YesOrNo.N;
	
	@Column(name = "USER_JOIN_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date userJoinDate;
	
	@Column(name = "USER_PURCHASE_CNT")
	private int userPurchaseCnt;
	
	// 회원 탈퇴
	public void withdraw() {
		this.userIsWithdrawn = YesOrNo.Y;
	}
	
	// 회원 탈퇴 취소
	public void reactivate() {
		this.userIsWithdrawn = YesOrNo.N;
	}
}
