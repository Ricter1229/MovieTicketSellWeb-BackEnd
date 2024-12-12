package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.AuditoriumBean;
@Repository
public interface AuditoriumRepository extends JpaRepository<AuditoriumBean, Integer> {

	List<AuditoriumBean> findByStoreId(Integer storeId);

}
