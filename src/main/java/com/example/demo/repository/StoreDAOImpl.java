package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.StoreBean;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;
@Transactional
@Repository
public class StoreDAOImpl implements StoreDAO {
	
	@PersistenceContext
	private Session session;

	public Session getSession() {
		return this.session;
	}
	
	@Override
	public List<StoreBean> find(JSONObject obj) {
		String findName;
		try {
				findName = obj.isNull("findName") ? null : obj.getString("findName");
				String fullName = obj.isNull("fullName") ? null : obj.getString("fullName");
				String order = obj.isNull("order") ? "id" : obj.getString("order");
				boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
				Integer start = obj.isNull("start") ? null : obj.getInt("start");
				Integer max = obj.isNull("max") ? null : obj.getInt("max");
				Integer regionFilter=obj.isNull("regionFilter") ? null : obj.getInt("regionFilter");
			
			CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
			CriteriaQuery<StoreBean> criteriaQery = criteriaBuilder.createQuery(StoreBean.class);
			
			Root<StoreBean> table = criteriaQery.from(StoreBean.class);
			
			List<Predicate> predicates = new ArrayList<>();
			
			if(findName!=null && findName.length()!=0) {
				Predicate p = criteriaBuilder.like(table.get("name"), "%"+findName+"%");
				predicates.add(p);
			}
			if(regionFilter!=null) {
				Predicate p = criteriaBuilder.equal(table.get("region"), regionFilter);
				predicates.add(p);
			}
			if(fullName!=null) {
				Predicate p = criteriaBuilder.equal(table.get("name"), fullName);
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
			
			TypedQuery<StoreBean> typedQuery = this.getSession().createQuery(criteriaQery);
			if(start!=null) {
				typedQuery = typedQuery.setFirstResult(start);
			}
			if(max!=null) {
				typedQuery = typedQuery.setMaxResults(max);
			}
	
			List<StoreBean> result = typedQuery.getResultList();
			if(result!=null && !result.isEmpty()) {
				return result;
			} 
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long count(JSONObject obj) {
		String findName;
		String fullName;
		try {
			findName = obj.isNull("findName") ? null : obj.getString("findName");
			fullName = obj.isNull("fullName") ? null : obj.getString("fullName");
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQery = criteriaBuilder.createQuery(Long.class);
		
		Root<StoreBean> table = criteriaQery.from(StoreBean.class);
		
		criteriaQery = criteriaQery.select(criteriaBuilder.count(table));

		List<Predicate> predicates = new ArrayList<>();
		if(findName!=null && findName.length()!=0) {
			Predicate p = criteriaBuilder.like(table.get("name"), "%"+findName+"%");
			predicates.add(p);
		}
		if(fullName!=null && fullName.length()!=0) {
			Predicate p = criteriaBuilder.equal(table.get("name"), fullName);
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
		} 
		
		} catch (JSONException e) {

			e.printStackTrace();
		}
		
			return 0;

	}


}
