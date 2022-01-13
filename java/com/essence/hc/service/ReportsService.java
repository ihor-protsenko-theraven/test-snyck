package com.essence.hc.service;

import java.util.Date;

import com.essence.hc.eil.parsers.StepCountReport;

public interface ReportsService {

	public StepCountReport getStepCount(String accountIdentifier, String panelId, String deviceId, 
			Date startTime, Date endTime, String timePart);
	
}
