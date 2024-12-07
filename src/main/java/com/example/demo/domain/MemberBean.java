package com.example.demo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="Member")
public class MemberBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="account")
	private String account;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone_no")
	private String phoneNo;
	
	@Column(name="birth_date")
	private Date birthDate;

	@PrePersist
	public void init() {
		if (birthDate == null) {
			birthDate = new Date();
		}
	}
	
	@JsonManagedReference("member")
	@OneToMany(mappedBy = "member")
	private List<MemberBuyTicketOrderBean> memberBuyTicketOrders = new ArrayList<>();

	@Override
	public String toString() {
		return "MemberBean [id=" + id + ", account=" + account + ", password=" + password + ", email=" + email
				+ ", phoneNo=" + phoneNo + ", birthDate=" + birthDate + "]";
	}
}
