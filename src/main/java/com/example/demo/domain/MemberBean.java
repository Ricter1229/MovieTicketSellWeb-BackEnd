package tw.com.ispan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Member")
public class MemberBean {
	@Id
	@Column(name = "account")
	private String account;

	@Override
	public String toString() {
		return "MemeberBean [account=" + account + ", password=" + password + ", email=" + email
				+ ", phoneNo=" + phoneNo + ", birthDate=" + birthDate + "]";
	}


	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "phoneNo")
	private String phoneNo;

	@Column(name = "birthDate")
	private java.util.Date birthDate;
	
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

}
