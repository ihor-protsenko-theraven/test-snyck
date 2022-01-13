package com.essence.hc.persistenceexternal.impl;

import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalReportsDAO;
import com.essence.hc.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.essence.hc.eil.parsers.StepCountReport;

public class ExternalReportsDAOImpl extends ExternalDAOImpl implements ExternalReportsDAO{
	
	public StepCountReport getStepCountReport(String accountIdentifier, String panelId, String deviceId, 
			List<HashMap<String, String>> timeRanges, String timePart){
		
		HashMap<String, Object> reqParams = new HashMap<String,Object>();
		
		reqParams.put("account", accountIdentifier);
		
		if(accountIdentifier == null || accountIdentifier.isEmpty()) { reqParams.put("panelId", Integer.parseInt(panelId)); }
		if(deviceId != null) { reqParams.put("deviceIdentifier", deviceId); }
		
		reqParams.put("timeRanges", timeRanges);
		
		if( timePart != null) { reqParams.put("timePart", timePart); }
		else { reqParams.put("timeResolution", "Day"); }
		
		
		if (Util.isDebug()) {
			return (StepCountReport) performDummyRequest(List.class, "external_get_step_count", reqParams);
		}
		
		return (StepCountReport) performRequest(List.class, "external_get_step_count", reqParams);
		
	}
	
	public List<HashMap<String, String>> obtainReportTimeRange(Date startTime, Date endTime){
		
		DateFormat dateFormat = new SimpleDateFormat(Util.DATETIME_TMZ_FORMAT_INPUT);
		
		List<HashMap<String, String>> timeRanges = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> timeRange = new HashMap<String, String>();
		
		if(startTime != null) {
			timeRange.put("startTime", dateFormat.format(startTime));
			timeRange.put("startTime", dateFormat.format(startTime));
		}
		
		
		if(endTime != null) {
			timeRange.put("endTime", dateFormat.format(endTime));
			timeRange.put("endTime", dateFormat.format(endTime));
		}
		
		
		timeRanges.add(timeRange);
		
		return timeRanges;
	}

}
