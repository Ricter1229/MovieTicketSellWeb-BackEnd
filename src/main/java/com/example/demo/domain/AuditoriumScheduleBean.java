package com.example.demo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name="AuditoriumSchedule")
public class AuditoriumScheduleBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@JsonBackReference("auditoriumScheduleBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "auditoriumId",
			referencedColumnName = "id"
	)
	private AuditoriumBean auditoriumBean;
	
	@JsonBackReference("auditoriumScheduleBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "storeReleaseMovieId",
			referencedColumnName = "id"
	)
	private StoreReleaseMovieBean storeReleaseMovieBean;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="timeSlots")
	private String timeSlots;
	
	@Column(name="soldSeatsJson")
	private byte[] soldSeatsJson;
	
	@JsonManagedReference("auditoriumScheduleBean")
	@OneToMany(mappedBy = "auditoriumScheduleBean")
	private List<MemberBuyTicketDetailBean> memberBuyTicketDetailBeans = new ArrayList<>();
	
	@JsonManagedReference("auditoriumScheduleBean")
	@OneToMany(mappedBy = "auditoriumScheduleBean")
	private List<SeatingListBean> SeatingListBeans = new ArrayList<>();
}
