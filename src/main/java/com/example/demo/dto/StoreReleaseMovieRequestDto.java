package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class StoreReleaseMovieRequestDto {
	private Integer storeId;
	private Integer versionId;
    private List<MovieDto> storeReleaseMovie;
    
    @Data
	public static class MovieDto {
        private Integer storeId;     // 商店 ID
        private List<Integer> movieVersionIds;     // 电影 ID
    }
}
