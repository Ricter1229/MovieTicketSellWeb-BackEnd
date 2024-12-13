package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.StoreReleaseMovieBean;
import com.example.demo.dto.StoreReleaseMovieRequestDto;
import com.example.demo.dto.internal.StoreInternalDto;
import com.example.demo.service.StoreReleaseMovieService;

@RestController
@CrossOrigin
@RequestMapping("/api/store-release-movie")
public class StoreReleaseMovieController {
	@Autowired
	private StoreReleaseMovieService storeReleaseMovieService;

	@PostMapping("/")
	public ApiResponse<Object> updateStoreMovie(@RequestBody StoreReleaseMovieRequestDto request) {
		System.out.println(request);
		storeReleaseMovieService.deleteAllWithStoreId(request.getStoreReleaseMovie().get(0).getStoreId());
		List<StoreReleaseMovieBean> reqponse = storeReleaseMovieService.insert(
				request.getStoreReleaseMovie().get(0).getStoreId(),
				request.getStoreReleaseMovie().get(0).getMovieVersionIds());
		return ApiResponse.success(reqponse);
	}

	@PostMapping("/all")
	public ApiResponse<Object> getAll(@RequestBody StoreReleaseMovieRequestDto request) {
		return ApiResponse.success(storeReleaseMovieService.getAll(request.getStoreId()));
	}

	@PostMapping("/all-with-storeid-versionid")
	public ApiResponse<Object> getAllWithStoreIdAndVersionId(@RequestBody StoreReleaseMovieRequestDto request) {
		return ApiResponse.success(
				storeReleaseMovieService.findByStoreIdAndVersionId(request.getStoreId(), request.getVersionId()));
	}

	@PostMapping("/get-versions")
	public ApiResponse<Object> getVersions(@RequestBody StoreReleaseMovieRequestDto request) {
		return ApiResponse.success(storeReleaseMovieService.findStoreReleaseMovieVersions(request.getStoreId()));
	}

}
