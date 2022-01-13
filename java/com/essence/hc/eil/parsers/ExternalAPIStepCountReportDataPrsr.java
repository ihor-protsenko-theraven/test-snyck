package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.model.StepCountDeviceReport;

public class ExternalAPIStepCountReportDataPrsr implements IParser<StepCountReport>{
	
	String timeResolution;
	List<ExternalAPIStepCountDeviceReportPrsr> devices;
	
	@Override
	public StepCountReport parse() {
	
		StepCountReport stepCountReport = new StepCountReport();
		
		if(this.timeResolution != null) {
			stepCountReport.setTimeResolution(timeResolution);
		}
		
		if(this.devices != null) {
			
			List<StepCountDeviceReport> stepCountDeviceReports = new ArrayList<StepCountDeviceReport>();
			
			for(ExternalAPIStepCountDeviceReportPrsr stepCountDeviceReport : this.devices) {
				stepCountDeviceReports.add(stepCountDeviceReport.parse());
			}
			
			stepCountReport.setStepCountDeviceReports(stepCountDeviceReports);
		}
		
		return stepCountReport;
	
	}

	public String getTimeResolution() {
		return timeResolution;
	}

	public void setTimeResolution(String timeResolution) {
		this.timeResolution = timeResolution;
	}

	public List<ExternalAPIStepCountDeviceReportPrsr> getDevices() {
		return devices;
	}

	public void setDevices(List<ExternalAPIStepCountDeviceReportPrsr> devices) {
		this.devices = devices;
	}

}
