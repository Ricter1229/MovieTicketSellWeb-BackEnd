package com.example.demo.dto;

import java.util.List;

public record FindMovieResponseRecord(long count ,List<FindMovieResponseDTO> list ,boolean success , String message) {

}
