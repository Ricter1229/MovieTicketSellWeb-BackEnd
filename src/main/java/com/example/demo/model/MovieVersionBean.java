package com.example.demo.model;

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
@Table(name="MovieVersion")
public class MovieVersionBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="movieId")
	private Integer movieId;
	
	@JsonBackReference("MovieVersionBeans")
    @ManyToOne
	@JoinColumn(
			insertable = false, 
			updatable = false,
			name = "movieId",
			referencedColumnName = "id"
	)
	private MovieBean movie;
	
	@Column(name="version")
	private String version;
	
	@JsonManagedReference("movieVersion")
	@OneToMany(mappedBy = "movieVersion")
	private List<StoreReleaseMovieBean> storeReleaseMovieBeans = new ArrayList<>();
}
