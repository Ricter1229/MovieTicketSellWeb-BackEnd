package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.VersionBean;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.VersionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionService {
	@Autowired
	private VersionRepository versionRepository;
	
	public List<VersionBean> getAll() {
		return versionRepository.findAll();
	}
	
	public VersionBean getOne(Integer versionId) {
		VersionBean version = versionRepository.findById(versionId)
		 	.orElseThrow(() -> new CustomException("Version not found", 404));
		return version;
	}

	public List<VersionBean> getAllVersionHaveAlestOneMovie() {
	    return versionRepository.getAllWithMovies();
	}
}
