package com.lcpk.mtype.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SELLER_TB")
public class SellerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq")
	@SequenceGenerator(name = "seller_seq", sequenceName = "SELLER_SQ", allocationSize = 1)

	@Column(name = "SELLER_NO")
	private Long sellerNo;

	@Column(name = "SELLER_ID", nullable = false, unique = true, length = 30)
	private String sellerId;

	@Column(name = "SELLER_PW", nullable = false, length = 255)
	private String sellerPw;

	@Column(name = "SELLER_NM", nullable = false, length = 20)
	private String sellerNm;

	@Column(name = "SELLER_TEL", nullable = false, unique = true, length = 20)
	private String sellerTel;

	@Column(name = "SELLER_EMAIL", length = 30)
	private String sellerEmail;

	@Column(name = "BUSINESS_NO", nullable = false, unique = true, length = 20)
	private String businessNo;

	@Column(name = "BUSINESS_NM", nullable = false, length = 30)
	private String businessNm;

	@Column(name = "BUSINESS_ADDR", nullable = false, length = 100)
	private String businessAddr;

	@Column(name = "IS_DELETED", length = 1)
	private String isDeleted = "N";

	@Column(name = "IS_ALLOWED", length = 1)
	private String isAllowed = "N";

	@Column(name = "JOINED_DATE")
	private LocalDate joinedDate;

	@Column(name = "BUSINESS_DETAIL_ADDR")
	private String businessDetailAddr;
	
	@Column(name = "REFRESH_TOKEN")
	private String refreshToken;
	
	@Column(name = "SELLER_ROLE", length = 20)
	private String role = "SELLER";
	
	// 기본 생성자
	public SellerEntity() {
		super();
	}

	// 매개변수 생성자
	public SellerEntity(Long sellerNo, String sellerId, String sellerPw, String sellerNm, String sellerTel,
			String sellerEmail, String businessNo, String businessNm, String businessAddr, String isDeleted,
			String isAllowed, LocalDate joinedDate, String businessDetailAddr) {
		super();
		this.sellerNo = sellerNo;
		this.sellerId = sellerId;
		this.sellerPw = sellerPw;
		this.sellerNm = sellerNm;
		this.sellerTel = sellerTel;
		this.sellerEmail = sellerEmail;
		this.businessNo = businessNo;
		this.businessNm = businessNm;
		this.businessAddr = businessAddr;
		this.isDeleted = isDeleted;
		this.isAllowed = isAllowed;
		this.joinedDate = joinedDate;
		this.businessDetailAddr = businessDetailAddr;
	}

	// Getter & Setter
	public Long getSellerNo() {
		return sellerNo;
	}

	public void setSellerNo(Long sellerNo) {
		this.sellerNo = sellerNo;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerPw() {
		return sellerPw;
	}

	public void setSellerPw(String sellerPw) {
		this.sellerPw = sellerPw;
	}

	public String getSellerNm() {
		return sellerNm;
	}

	public void setSellerNm(String sellerNm) {
		this.sellerNm = sellerNm;
	}

	public String getSellerTel() {
		return sellerTel;
	}

	public void setSellerTel(String sellerTel) {
		this.sellerTel = sellerTel;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getBusinessNm() {
		return businessNm;
	}

	public void setBusinessNm(String businessNm) {
		this.businessNm = businessNm;
	}

	public String getBusinessAddr() {
		return businessAddr;
	}

	public void setBusinessAddr(String businessAddr) {
		this.businessAddr = businessAddr;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(String isAllowed) {
		this.isAllowed = isAllowed;
	}

	public LocalDate getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(LocalDate joinedDate) {
		this.joinedDate = joinedDate;
	}
	
	public String getBusinessDetailAddr() {
		return businessDetailAddr;
	}
	
	public void setBusinessDetailAddr(String businessDetailAddr) {
		this.businessDetailAddr = businessDetailAddr;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
