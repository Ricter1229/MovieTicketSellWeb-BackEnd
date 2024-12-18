package com.example.demo.repository;

import java.util.List;

import org.json.JSONObject;

import com.example.demo.domain.MovieBean;

public interface MovieDAO {
	public abstract long count(JSONObject obj);

	public abstract List<MovieBean> find(JSONObject obj);

	// 吳其容修改
	List<MovieBean> findByDateRange();

	List<MovieBean> findByGreaterReleasedDate();
}
