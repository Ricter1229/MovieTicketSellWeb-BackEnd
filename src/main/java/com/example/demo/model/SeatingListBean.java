package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SeatingList")
public class SeatingListBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@Column(name = "auditoriumScheduleId")
    private Integer auditoriumScheduleId;
	
	@JsonBackReference("auditoriumScheduleBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "auditoriumScheduleId",
			referencedColumnName = "id"
	)
	private AuditoriumScheduleBean auditoriumScheduleBean;
	
	@Column(name = "seat") // 座位編號
    private String seat;
	
	@Column(name = "isLocked") // 是否被鎖定
    private Integer isLocked;
	
	@Column(name = "isSold") // 是否已售出
    private Integer isSold;
	
	@Column(name = "lockedByOrderId") // 鎖定的訂單 Id
    private Integer lockedByOrderId;
	
	@Column(name = "soldByOrderId") // 購買的訂單 Id
    private Integer soldByOrderId;
	
	@Column(name = "seatLockCreateTime") // 座位開始鎖住的時間
    private LocalDateTime seatLockCreateTime;
	
	@PrePersist
	private void init() {
		if(isLocked == null) {
			isLocked = 0;
		}
		if(isSold == null) {
			isSold = 0;
		}
	}
}
