package com.example.demo.domain;


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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Auditorium")
public class AuditoriumBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@JsonBackReference("auditoriumBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "storeId",
			referencedColumnName = "id"
	)
	private StoreBean store;
	
	@Column(name="storeId")
	private Integer storeId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="seatingList")
	private String seatingList;
	
	@JsonManagedReference("auditoriumBean")
	@OneToMany(mappedBy = "auditoriumBean", cascade = CascadeType.ALL)
	private List<AuditoriumScheduleBean> auditoriumScheduleBeans = new ArrayList<>();
}
