package com.essence.hc.persistenceexternal;

import java.util.HashMap;
import java.util.List;

import com.essence.hc.eil.parsers.StepCountReport;

public interface ExternalReportsDAO{

	public StepCountReport getStepCountReport(String accountIdentifier, String panelId, String deviceId, 
			List<HashMap<String, String>> timeRanges, String timePart);
}
