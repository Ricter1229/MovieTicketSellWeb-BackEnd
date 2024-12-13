package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.domain.MovieVersionBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreReleaseMovieBean;
import com.example.demo.domain.VersionBean;
import com.example.demo.dto.internal.StoreInternalDto;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.StoreReleaseMovieRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.VersionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StoreReleaseMovieService {
	@Autowired
	private StoreReleaseMovieRepository storeReleaseMovieRepository;
	@Autowired
	private MovieVersionService movieVersionService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private AuditoriumScheduleRepository auditoriumScheduleRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private VersionRepository versionRepository;

	public List<StoreReleaseMovieBean> insert(Integer storeId, List<Integer> movieVersionIds) {
		List<StoreReleaseMovieBean> storeReleaseMovieList = new ArrayList<>();
		for (Integer movieVersionId : movieVersionIds) {
			StoreReleaseMovieBean storeReleaseMovie = new StoreReleaseMovieBean();
			MovieVersionBean movieVersion = movieVersionService.findById(movieVersionId);
			storeReleaseMovie.setMovieVersionId(movieVersion.getId());
			storeReleaseMovie.setMovieVersion(movieVersion);

			storeReleaseMovie.setStore(storeService.findById(storeId));
			storeReleaseMovie.setStoreId(storeId);
			storeReleaseMovie.setAuditoriumScheduleBeans(null);

			storeReleaseMovieList.add(storeReleaseMovie);
		}
		return storeReleaseMovieRepository.saveAll(storeReleaseMovieList);
	}

	public boolean deleteAllWithStoreId(Integer storeId) {
		List<StoreReleaseMovieBean> storeReleaseMovies = storeReleaseMovieRepository.findByStoreId(storeId);
		if (storeReleaseMovies.isEmpty()) {
			System.out.println("No storeReleaseMovies found for store ID: " + storeId);
			return false;
		}

		// 手动删除关联的 AuditoriumScheduleBean 数据
		for (StoreReleaseMovieBean storeReleaseMovie : storeReleaseMovies) {
			List<AuditoriumScheduleBean> auditoriumSchedules = storeReleaseMovie.getAuditoriumScheduleBeans();
			if (auditoriumSchedules != null && !auditoriumSchedules.isEmpty()) {
				auditoriumScheduleRepository.deleteAll(auditoriumSchedules);
			}
		}
		storeReleaseMovieRepository.deleteAll(storeReleaseMovies);
		return true;
	}

	public Object getAll(Integer storeId) {
		try {
            List<StoreBean> stores = storeRepository.findAll();
            List<VersionBean> versions = versionRepository.findAll();
            List<StoreReleaseMovieBean> storeReleaseMovies = storeReleaseMovieRepository.findByStoreId(storeId);

            
            List<Map<String, Object>> response = new ArrayList<>();
            for(StoreReleaseMovieBean storeReleaseMovie : storeReleaseMovies) {
                Map<String, Object> movie = new HashMap<>();
            	movie.put("movieVersionId", storeReleaseMovie.getMovieVersionId());
            	movie.put("versionId", storeReleaseMovie.getMovieVersion().getVersionId());
            	movie.put("movie", storeReleaseMovie.getMovieVersion().getMovie());
            	response.add(movie);
            }
            
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public List<Map<String, Object>> findByStoreIdAndVersionId(Integer storeId, Integer versionId) {
		try {
    		List<StoreReleaseMovieBean> storeReleaseMovies = storeReleaseMovieRepository.findByStoreIdAndVersionId(storeId, versionId);

            
            List<Map<String, Object>> response = new ArrayList<>();
            for(StoreReleaseMovieBean storeReleaseMovie : storeReleaseMovies) {
                Map<String, Object> movie = new HashMap<>();
            	movie.put("movieVersionId", storeReleaseMovie.getMovieVersionId());
            	movie.put("versionId", storeReleaseMovie.getMovieVersion().getVersionId());
            	movie.put("movie", storeReleaseMovie.getMovieVersion().getMovie());
            	movie.put("storeReleaseMovieId", storeReleaseMovie.getId());
            	response.add(movie);
            }
            
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public List<VersionBean> findStoreReleaseMovieVersions(Integer storeId) {
		return storeReleaseMovieRepository.findStoreReleaseMovieVersions(storeId);
	}
	
}
