package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.MovieBean;
import com.example.demo.domain.MovieVersionBean;
import com.example.demo.domain.StoreReleaseMovieBean;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.MovieVersionRepository;
import com.example.demo.repository.StoreReleaseMovieRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class MovieVersionService {
	@Autowired
	private MovieVersionRepository movieVersionRepository;
	@Autowired
	private VersionService versionService;
	@Autowired
	private StoreReleaseMovieRepository storeReleaseMovieRepository;

	public MovieVersionBean insert(Integer movieId, Integer versionId, MovieBean movie) {
		System.out.println(movieId);
		if (movieId == null || versionId == null || movie == null) {
	        throw new IllegalArgumentException("Invalid input: movieId, version, or movie cannot be null.");
	    }

	    // 检查是否已存在相同版本
	    List<MovieVersionBean> existingVersions = movieVersionRepository.findByMovieId(movieId);
	    for (MovieVersionBean existingVersion : existingVersions) {
	        if (existingVersion.getVersionId().equals(versionId)) {
	            return null;
	        }
	    }
		System.out.println(movieId);

		MovieVersionBean movieVersion = new MovieVersionBean();
		movieVersion.setMovieId(movieId);
		movieVersion.setVersionId(versionId);
		movieVersion.setVersionBean(versionService.getOne(versionId));
		movieVersion.setMovie(movie);
		return movieVersionRepository.save(movieVersion);
	}
	
	public boolean deleteAll(Integer movieId) {
		if (movieId == null) {
	        throw new IllegalArgumentException("Movie ID cannot be null.");
	    }
	    List<MovieVersionBean> versionList = movieVersionRepository.findByMovieId(movieId);
	    if (versionList.isEmpty()) {
	        System.out.println("No versions found for movie ID: " + movieId);
	        return false; // 如果没有版本信息，直接返回
	    }
	    for(MovieVersionBean version : versionList) {

	    	List<StoreReleaseMovieBean> delete = storeReleaseMovieRepository.findByMovieVersionId(version.getId());
	    	storeReleaseMovieRepository.deleteAll(delete);
	    	
	    }
    	movieVersionRepository.deleteAll(versionList);
    	movieVersionRepository.flush();
	    return true;
	}
	
	public List<MovieVersionBean> getAll(Integer movieId) {
		if (movieId == null) {
	        throw new IllegalArgumentException("Movie ID cannot be null.");
	    }
		return movieVersionRepository.findByMovieId(movieId);
	}
	
	
	public Page<MovieVersionBean> getOneVersionAllMoviesWithVersions(Integer versionId, Integer page, Integer size) {
	 	if (versionId == null) {
	        throw new IllegalArgumentException("versionId cannot be null");
	    }

	    Pageable pageable = PageRequest.of(page, size);
	    return movieVersionRepository.findMovieVersionsByVersionId(versionId, pageable);
	}

	public MovieVersionBean findById(Integer movieVersionId) {
		return movieVersionRepository.findById(movieVersionId)
			.orElseThrow(() -> new CustomException("MovieVersionId not found", 404));
	}

	public MovieVersionBean findByMovieIdAndVersionId(Integer movieId, Integer versionId) {
		return movieVersionRepository.findByMovieIdAndVersionId(movieId, versionId)
				.orElseThrow(() -> new CustomException("MovieVersionId not found", 404));
	}

}
