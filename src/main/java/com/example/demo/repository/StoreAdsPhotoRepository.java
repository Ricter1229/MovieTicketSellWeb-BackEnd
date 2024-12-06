package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.StoreAdsPhotoBean;

public interface StoreAdsPhotoRepository extends JpaRepository<StoreAdsPhotoBean, Integer> {
	@Query("from StoreAdsPhotoBean order by sortId")
	public List<StoreAdsPhotoBean> findAdsAllSorted();
	@Query(value="select * from StoreAdsPhoto where sortId=:sortId",nativeQuery=true)
	public List<StoreAdsPhotoBean> findAdsBySortedId(@Param(value="sortId")Integer sortId);
}
