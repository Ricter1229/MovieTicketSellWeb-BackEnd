package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MovieBean;
import com.example.demo.domain.MovieVersionBean;
import com.example.demo.dto.MovieVersionRequestDto;
import com.example.demo.service.MovieVersionService;

@RestController
@CrossOrigin
@RequestMapping("/api/movie-version")
public class MovieVersionController {
	@Autowired
	private MovieVersionService movieVersionService;
	
	@PostMapping("/")
	public ApiResponse<Object> getMoviesByVersionId(@RequestBody MovieVersionRequestDto request ) {
		if (request.getPage() < 0) {
		    request.setPage(0); // 默认第一页
		}
		if (request.getSize() <= 0) {
		    request.setSize(5); // 默认每页10条
		}
		
		try {
	        if (request.getVersionId() == null) {
	            throw new IllegalArgumentException("Version ID cannot be null");
	        }

	        Page<MovieVersionBean> pageMovieVersions = movieVersionService.getOneVersionAllMoviesWithVersions(
	                request.getVersionId(),
	                request.getPage(),
	                request.getSize()
	        );

	        long totalItems = pageMovieVersions.getTotalElements();
	        int totalPages = pageMovieVersions.getTotalPages();

	        List<Map<String, Object>> moviesDto = pageMovieVersions.getContent()
	                .stream()
	                .map(mv -> {
	                    Map<String, Object> movieVersionMap = new HashMap<>();
	                    movieVersionMap.put("movieVersionId", mv.getId());
	                    
	                    // 将 movie 数据嵌套
	                    Map<String, Object> movieMap = new HashMap<>();
	                    MovieBean movie = mv.getMovie();
	                    movieMap.put("movieId", movie.getId());
	                    movieMap.put("chineseName", movie.getChineseName());
	                    movieMap.put("mimeType", movie.getMimeType());
	                    movieMap.put("photo", movie.getPhoto());

	                    movieVersionMap.put("movie", movieMap);
	                    return movieVersionMap;
	                })
	                .collect(Collectors.toList());

	        Map<String, Object> response = new HashMap<>();
	        response.put("movies", moviesDto);
	        response.put("currentPage", request.getPage());
	        response.put("totalItems", totalItems);
	        response.put("totalPages", totalPages);
	        response.put("versionId", request.getVersionId());
	        
	        return ApiResponse.success(response);
	    } catch (IllegalArgumentException e) {
	        return ApiResponse.fail(400, null, e.getMessage());
	    } catch (Exception e) {
	        return ApiResponse.fail(500, null, "Internal server error: " + e.getMessage());
	    }
	} 
}
