package com.example.demo.dto;

import java.util.List;

import com.example.demo.domain.MovieBean;

public record MovieResponse (long count ,List<MovieBean> list ,boolean success , String message) {

}
