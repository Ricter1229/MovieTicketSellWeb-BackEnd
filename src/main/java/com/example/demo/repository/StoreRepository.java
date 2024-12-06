package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.StoreBean;


@Repository
public interface StoreRepository extends JpaRepository<StoreBean, Integer>,StoreDAO {
	@Query(value="from StoreBean where region=:region")
	public List<StoreBean> findStoreByRegion(@Param(value="region")Integer region);
	@Query("SELECT DISTINCT s.region FROM StoreBean s")
    List<Integer> findDistinctRegions();
	@Query(value="from StoreBean where name=:name")
	public List<StoreBean> findStoreByName(@Param(value="name")String name);
}
