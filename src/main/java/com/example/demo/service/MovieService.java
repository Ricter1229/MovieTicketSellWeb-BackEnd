package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.MovieBean;
import com.example.demo.repository.MovieRepository;
import com.example.demo.util.DatetimeConverter;

@Service
@Transactional
public class MovieService {
	
	@Autowired
	private MovieRepository movieRepo;
	
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return movieRepo.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<MovieBean> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return movieRepo.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MovieBean findById(Integer id) {
		if(id!=null) {
			Optional<MovieBean> optional = movieRepo.findById(id);
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	public boolean exists(Integer id) {
		if(id!=null) {
			return movieRepo.existsById(id);
		}
		return false;
	}
	
	public MovieBean create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String chineseName = obj.isNull("chineseName") ? null : obj.getString("chineseName");
			String englishName = obj.isNull("englishName") ? null : obj.getString("englishName");
			String cast = obj.isNull("cast") ? null : obj.getString("cast");
			String releasedDate = obj.isNull("releasedDate") ? null : obj.getString("releasedDate");
			String outOfDate = obj.isNull("outOfDate") ? null : obj.getString("outOfDate");
			String intro = obj.isNull("intro") ? null : obj.getString("intro");
			Double price = obj.isNull("price") ? null : obj.getDouble("price");
			Integer ticketCount = obj.isNull("ticketCount") ? null : obj.getInt("ticketCount");
			String style = obj.isNull("style") ? null : obj.getString("style");
			String rating = obj.isNull("rating") ? null : obj.getString("rating");
			String runTime = obj.isNull("runTime") ? null : obj.getString("runTime");
			String commercialFilmURL = obj.isNull("commercialFilmURL") ? null : obj.getString("commercialFilmURL");
			
					
			MovieBean insert = new MovieBean();
			insert.setChineseName(chineseName);
			insert.setEnglishName(englishName);
			insert.setCast(cast);
			insert.setReleasedDate(DatetimeConverter.parse(releasedDate, "yyyy-MM-dd"));
			insert.setOutOfDate(DatetimeConverter.parse(outOfDate, "yyyy-MM-dd"));
			insert.setIntro(intro);
			insert.setPrice(price);
			insert.setTicketCount(ticketCount);
			insert.setStyle(style);
			insert.setRating(rating);
			insert.setRunTime(runTime);
			insert.setCommercialFilmURL(commercialFilmURL);
			insert.setPhoto(null);
			return movieRepo.save(insert);
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MovieBean modify(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer id = obj.isNull("id") ? null : obj.getInt("id");
			String chineseName = obj.isNull("chineseName") ? null : obj.getString("chineseName");
			String englishName = obj.isNull("englishName") ? null : obj.getString("englishName");
			String cast = obj.isNull("cast") ? null : obj.getString("cast");
			String releasedDate = obj.isNull("releasedDate") ? null : obj.getString("releasedDate");
			String outOfDate = obj.isNull("outOfDate") ? null : obj.getString("outOfDate");
			String intro = obj.isNull("intro") ? null : obj.getString("intro");
			Double price = obj.isNull("price") ? null : obj.getDouble("price");
			Integer ticketCount = obj.isNull("ticketCount") ? null : obj.getInt("ticketCount");
			String style = obj.isNull("style") ? null : obj.getString("style");
			String rating = obj.isNull("rating") ? null : obj.getString("rating");
			String runTime = obj.isNull("runTime") ? null : obj.getString("runTime");
			String commercialFilmURL = obj.isNull("commercialFilmURL") ? null : obj.getString("commercialFilmURL");
			
			
			
			if (id != null) {
				Optional<MovieBean> optional = movieRepo.findById(id);
				if(optional.isPresent()) {
					MovieBean update = optional.get();
					update.setChineseName(chineseName);
					update.setEnglishName(englishName);
					update.setCast(cast);
					update.setReleasedDate(DatetimeConverter.parse(releasedDate, "yyyy-MM-dd"));
					update.setOutOfDate(DatetimeConverter.parse(outOfDate, "yyyy-MM-dd"));
					update.setIntro(intro);
					update.setPrice(price);
					update.setTicketCount(ticketCount);
					update.setStyle(style);
					update.setRating(rating);
					update.setRunTime(runTime);
					update.setCommercialFilmURL(commercialFilmURL);
					update.setPhoto(null);
					return movieRepo.save(update);
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean remove(Integer id) {
		if(id!=null && movieRepo.existsById(id)) {
			movieRepo.deleteById(id);
			return true;
		}
		return false;
	}
	
	public List<MovieBean> select(MovieBean bean) {
		List<MovieBean> result = null;
		if(bean!=null && bean.getId()!=null && !bean.getId().equals(0)) {
			Optional<MovieBean> optional = movieRepo.findById(bean.getId());
			if(optional.isPresent()) {
				result = new ArrayList<MovieBean>();
				result.add(optional.get());
			}
		} else {
			result = movieRepo.findAll();
		}
		return result;
	}
	
	public MovieBean insert(MovieBean bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<MovieBean> optional = movieRepo.findById(bean.getId());
			if(optional.isEmpty()) {
				return movieRepo.save(bean);
			}
		}
		return null;
	}
	
	public MovieBean update(MovieBean bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<MovieBean> optional = movieRepo.findById(bean.getId());
			if(optional.isPresent()) {
				return movieRepo.save(bean);
			}
		}
		return null;
	}
	
	public boolean delete(MovieBean bean) {
		if(bean!=null && bean.getId()!=null) {
			Optional<MovieBean> optional = movieRepo.findById(bean.getId());
			if(optional.isPresent()) {
				movieRepo.deleteById(bean.getId());
				return true;
			}
		}
		return false;
	}
}
