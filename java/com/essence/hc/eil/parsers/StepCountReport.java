package com.essence.hc.eil.parsers;

import java.util.List;

import com.essence.hc.model.StepCountDeviceReport;

public class StepCountReport {
	
	String timeResolution;
	List<StepCountDeviceReport> stepCountDeviceReports;
	
	public String getTimeResolution() {
		return timeResolution;
	}
	
	public void setTimeResolution(String timeResolution) {
		this.timeResolution = timeResolution;
	}
	
	public List<StepCountDeviceReport> getStepCountDeviceReports() {
		return stepCountDeviceReports;
	}
	
	public void setStepCountDeviceReports(List<StepCountDeviceReport> stepCountDeviceReports) {
		this.stepCountDeviceReports = stepCountDeviceReports;
	}
	
}
