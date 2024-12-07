package com.example.demo.repository;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.example.demo.domain.StoreBean;

public interface StoreDAO {
	public abstract long count(JSONObject obj);
	public abstract List<StoreBean> find(JSONObject obj);
}
