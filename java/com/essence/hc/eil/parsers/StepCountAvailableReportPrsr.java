package com.essence.hc.eil.parsers;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.StepCountAvailableReport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StepCountAvailableReportPrsr implements IParser<StepCountAvailableReport>{

	ExternalAPIWeareableDevicePrsr device;
	String firstStepCount;
	
	@Override
	public StepCountAvailableReport parse() {
		StepCountAvailableReport stepCountAvailableReport = new StepCountAvailableReport();
		
		stepCountAvailableReport.setWearableDevice(device.parse());
		stepCountAvailableReport.setFirstStepCount(firstStepCount);
		
		return stepCountAvailableReport;
	}

	public ExternalAPIWeareableDevicePrsr getDevice() {
		return device;
	}

	public void setDevice(ExternalAPIWeareableDevicePrsr device) {
		this.device = device;
	}

	public String getFirstStepCount() {
		return firstStepCount;
	}

	public void setFirstStepCount(String firstStepCount) {
		this.firstStepCount = firstStepCount;
	}

	
}
