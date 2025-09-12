package com.lcpk.mtype.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "RECENT_VIEW_TB")
public class RecentViewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recent_view_sq_generator")
	@SequenceGenerator(name = "recent_view_sq_generator", sequenceName = "RECENT_VIEW_SQ", allocationSize = 1)
	@Column(name = "RECENT_VIEW_NO")
	private Long recentViewNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_NO", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_NO", nullable = false)
	private ProductEntity product;
	
	@CreationTimestamp
	@Column(name = "VIEW_DATE")
	private LocalDateTime viewDate;
	
	@Builder
	public RecentViewEntity(User user, ProductEntity product) {
		this.user = user;
		this.product = product;
	}
	
	public void updateViewDate() {
		this.viewDate = LocalDateTime.now();
	}
}
