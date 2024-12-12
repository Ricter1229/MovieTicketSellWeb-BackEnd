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
@Table(name="StoreReleaseMovie")
public class StoreReleaseMovieBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
    
	@Column(name="MovieVersionId")
	private Integer movieVersionId;
	
    @JsonBackReference("storeReleaseMovieBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "MovieVersionId",
			referencedColumnName = "id"
	)
	private MovieVersionBean movieVersion;

    @Column(name="storeId")
	private Integer storeId;
    
    @JsonBackReference("storeReleaseMovieBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "storeId",
			referencedColumnName = "id"
	)
	private StoreBean store; 
    
    @JsonManagedReference("storeReleaseMovieBean")
	@OneToMany(mappedBy = "storeReleaseMovieBean", cascade = CascadeType.ALL)
	private List<AuditoriumScheduleBean> auditoriumScheduleBeans = new ArrayList<>();
}
