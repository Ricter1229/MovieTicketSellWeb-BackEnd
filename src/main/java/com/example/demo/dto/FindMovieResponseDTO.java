package com.example.demo.dto;

import java.util.List;

import com.example.demo.domain.MovieBean;

import lombok.Data;

@Data
public class FindMovieResponseDTO {
	
	MovieBean movie;
	String mainPhoto;
}
