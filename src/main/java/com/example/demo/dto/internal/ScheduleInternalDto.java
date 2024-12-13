package com.example.demo.dto.internal;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleInternalDto {
	private String version;
    private Date date;
    private String timeSlots;

    public ScheduleInternalDto(String version, Date date, String timeSlots) {
        this.version = version;
        this.date = date;
        this.timeSlots = timeSlots;
    }
}
