package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.MovieBean;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;

@Repository
public class MovieDAOImpl implements MovieDAO {
	
	@PersistenceContext
	private Session session;

	public Session getSession() {
		return this.session;
	}
	
	@Override
	public List<MovieBean> find(JSONObject obj) {
		String chineseName = obj.isNull("chineseName") ? null : obj.getString("chineseName");
		
		String order = obj.isNull("order") ? "id" : obj.getString("order");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		Integer start = obj.isNull("start") ? null : obj.getInt("start");
		Integer max = obj.isNull("max") ? null : obj.getInt("max");
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<MovieBean> criteriaQery = criteriaBuilder.createQuery(MovieBean.class);
		
		Root<MovieBean> table = criteriaQery.from(MovieBean.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(chineseName!=null && chineseName.length()!=0) {
			Predicate p = criteriaBuilder.like(table.get("chineseName"), "%"+chineseName+"%");
			predicates.add(p);
		}
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQery = criteriaQery.where(array);
		}
		
		if(dir) {
			criteriaQery = criteriaQery.orderBy(criteriaBuilder.desc(table.get(order)));
		} else {
			criteriaQery = criteriaQery.orderBy(criteriaBuilder.asc(table.get(order)));
		}
		
		TypedQuery<MovieBean> typedQuery = this.getSession().createQuery(criteriaQery);
		if(start!=null) {
			typedQuery = typedQuery.setFirstResult(start);
		}
		if(max!=null) {
			typedQuery = typedQuery.setMaxResults(max);
		}

		List<MovieBean> result = typedQuery.getResultList();
		if(result!=null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
	}

	@Override
	public long count(JSONObject obj) {
		String chineseName = obj.isNull("chineseName") ? null : obj.getString("chineseName");
		
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQery = criteriaBuilder.createQuery(Long.class);
		
		Root<MovieBean> table = criteriaQery.from(MovieBean.class);
		
		criteriaQery = criteriaQery.select(criteriaBuilder.count(table));

		List<Predicate> predicates = new ArrayList<>();
		
		if(chineseName!=null && chineseName.length()!=0) {
			Predicate p = criteriaBuilder.like(table.get("chineseName"), "%"+chineseName+"%");
			predicates.add(p);
		}
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQery = criteriaQery.where(array);
		}
		
		TypedQuery<Long> typedQuery = this.getSession().createQuery(criteriaQery);
		Long result = typedQuery.getSingleResult();
		if(result!=null) {
			return result.longValue();
		} else {
			return 0;
		}
	}


}
