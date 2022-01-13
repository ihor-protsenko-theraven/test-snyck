package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.StepCountDailyData;
import com.essence.hc.model.StepCountDeviceReport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIStepCountDeviceReportPrsr implements IParser<StepCountDeviceReport>{
	
	ExternalAPIWeareableDevicePrsr device;
	List<ExternalAPIStepCountDailyDataPrsr> stepCountInformation;

	
	@Override
	public StepCountDeviceReport parse() {
		
		StepCountDeviceReport stepCountDeviceReport = new StepCountDeviceReport();
		List<StepCountDailyData> stepCountDailyData = new ArrayList<StepCountDailyData>();
		
		stepCountDeviceReport.setWearableDevice(device.parse());
		
		if(stepCountInformation != null) {
			for(ExternalAPIStepCountDailyDataPrsr dailyReportData : stepCountInformation ) {
				stepCountDailyData.add(dailyReportData.parse());
			}
		}
		
		stepCountDeviceReport.setStepCountDailyData(stepCountDailyData);
		
		return stepCountDeviceReport;
	}
	

	public ExternalAPIWeareableDevicePrsr getDevice() {
		return device;
	}
	
	public void setDevice(ExternalAPIWeareableDevicePrsr device) {
		this.device = device;
	}


	public List<ExternalAPIStepCountDailyDataPrsr> getStepCountInformation() {
		return stepCountInformation;
	}


	public void setStepCountInformation(List<ExternalAPIStepCountDailyDataPrsr> stepCountInformation) {
		this.stepCountInformation = stepCountInformation;
	}
	
	
}
