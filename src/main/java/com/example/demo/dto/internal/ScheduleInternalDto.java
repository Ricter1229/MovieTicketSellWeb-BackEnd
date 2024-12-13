package com.example.demo.dto.internal;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleInternalDto {
	private String version;
    private Date date;
    private String timeSlots;
    private Integer storeReleaseMovieId;
    private Integer auditoriumId;
    private Integer versionId;
    private String auditoriumName;
    private Integer auditoriumScheduleId;
    
	public ScheduleInternalDto(Integer storeReleaseMovieId, Integer auditoriumId, String auditoriumName,
			Integer versionId, String version, Integer auditoriumScheduleId, Date date, String timeSlots) 
	{
		super();
		this.version = version;
		this.date = date;
		this.timeSlots = timeSlots;
		this.storeReleaseMovieId = storeReleaseMovieId;
		this.auditoriumScheduleId = auditoriumScheduleId;
		this.auditoriumId = auditoriumId;
		this.versionId = versionId;
		this.auditoriumName = auditoriumName;
	}

	public ScheduleInternalDto() {
		super();
	}
	
}
