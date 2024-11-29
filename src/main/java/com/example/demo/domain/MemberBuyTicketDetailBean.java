package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="MemberBuyTicketDetail")
public class MemberBuyTicketDetailBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="memberBuyTicketOrderId")
	private Integer memberBuyTicketOrderId;
	
	@JsonBackReference("memberBuyTicketDetailBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "memberBuyTicketOrderId",
			referencedColumnName = "id"
	)
	private MemberBuyTicketOrderBean memberBuyTicketOrderBean;

	@Column(name="storeId")
	private Integer storeId;
	
	@JsonBackReference("memberBuyTicketDetailBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "storeId",
			referencedColumnName = "id"
	)
	private StoreBean store;
	
	@Column(name="auditoriumScheduleId")
	private Integer auditoriumScheduleId;
	
    @JsonBackReference("memberBuyTicketDetailBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "auditoriumScheduleId",
			referencedColumnName = "id"
	)
	private AuditoriumScheduleBean auditoriumScheduleBean;
    
    @Column(name="seat")
	private String seat;
}
