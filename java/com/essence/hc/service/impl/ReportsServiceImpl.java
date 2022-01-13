package com.essence.hc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.eil.parsers.StepCountReport;
import com.essence.hc.persistenceexternal.impl.ExternalReportsDAOImpl;
import com.essence.hc.service.ReportsService;

public class ReportsServiceImpl implements ReportsService {

	@Autowired
	ExternalReportsDAOImpl externalReportsDAOImpl;
	
	@Override
	public StepCountReport getStepCount(String accountIdentifier, String panelId, String deviceId, 
			Date startTime, Date endTime, String timePart) {
		
		List<HashMap<String, String>> reportTimeRange = this.externalReportsDAOImpl.obtainReportTimeRange(startTime, endTime);
		
		return this.externalReportsDAOImpl.getStepCountReport(accountIdentifier, panelId, deviceId, reportTimeRange, timePart);
	}
	
}
