package com.example.demo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="MemberBuyTicketOrder")
public class MemberBuyTicketOrderBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "memberId")
    private Integer memberId;

    @JsonBackReference("memberBuyTicketOrders")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "memberId",
			referencedColumnName = "id"
	)
	private MemberBean member;
    
    @Column(name="movieId")
	private Integer movieId;
    
    @JsonBackReference("memberBuyTicketOrderBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "movieId",
			referencedColumnName = "id"
	)
	private MovieBean movieBean;
    
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @Column(name = "state")
    private String state;
    
    @Column(name = "merchantTradeNo")
    private String merchantTradeNo;
    
    @Column(name = "rtnCode")
    private Integer rtnCode;
    
    @Column(name = "paymentDate")
    private String paymentDate;
    
    @Column(name = "checkMacValue")
    private String checkMacValue;
    
    @Column(name = "tradeNo")
    private String tradeNo;
    
    @Column(name = "tradeDate")
    private String tradeDate;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timeBuying")
    private LocalDateTime timeBuying;
	
	@PrePersist
	public void init() {
		if (timeBuying == null) {
			timeBuying = LocalDateTime.now();
		}
	}
	
	@Column(name = "qrcode")
    private byte[] qrcode;
	
	@JsonManagedReference("memberBuyTicketOrderBean")
	@OneToMany(mappedBy = "memberBuyTicketOrderBean")
	private List<MemberBuyTicketDetailBean> memberBuyTicketDetailBeans = new ArrayList<>();
}
