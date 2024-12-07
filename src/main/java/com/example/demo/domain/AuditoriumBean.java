package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Auditorium")
@Setter
@Getter
@NoArgsConstructor
public class AuditoriumBean {
	@Id
	@Column(name = "id",columnDefinition = "DECIMAL(3, 0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storeId",referencedColumnName = "id")
	private StoreBean store;
	
	@Column(name = "name")
	private String auditoriumName;
	@Column(name = "seatingList")
	private String seatingList;
	
	@JsonManagedReference("auditoriumBean")
	@OneToMany(mappedBy = "auditoriumBean", cascade = CascadeType.ALL)
	private List<AuditoriumScheduleBean> auditoriumScheduleBeans = new ArrayList<>();
}
