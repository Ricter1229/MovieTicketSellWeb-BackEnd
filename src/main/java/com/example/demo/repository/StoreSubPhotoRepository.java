package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;

public interface StoreSubPhotoRepository extends JpaRepository<StoreSubPhotoBean,Integer> {
	@Query(value="select id from StoreSubPhoto where storeId=:storeId",nativeQuery = true)
	public List<Integer> findStoreSubIdByStoreId(@Param(value="storeId")Integer storeId);
	@Modifying
	@Query(value="delete from StoreSubPhoto where id=:id",nativeQuery = true)
	public int deleteStoreSubByStoreId(@Param(value="id")Integer id);
}
