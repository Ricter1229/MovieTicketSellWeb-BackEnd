package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Member")
public class MemberBean {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "account")
	private String account;

	@Override
	public String toString() {
		return "MemberBean [id=" + id + ", account=" + account + ", password=" + password + ", email=" + email
				+ ", phoneNo=" + phoneNo + ", birthDate=" + birthDate + "]";
	}

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "birth_date")
	private java.util.Date birthDate;
	
	@JsonManagedReference("member")
	@OneToMany(mappedBy = "member")
	private List<MemberBuyTicketOrderBean> memberBuyTicketOrders = new ArrayList<>();
	

	public List<MemberBuyTicketOrderBean> getMemberBuyTicketOrders() {
		return memberBuyTicketOrders;
	}
	public void setMemberBuyTicketOrders(List<MemberBuyTicketOrderBean> memberBuyTicketOrders) {
		this.memberBuyTicketOrders = memberBuyTicketOrders;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public java.util.Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(java.util.Date birthDate) {
		this.birthDate = birthDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
