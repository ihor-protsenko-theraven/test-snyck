package com.essence.hc.model;

import java.util.List;

public class StepCountDeviceReport {

	WearableDevice wearableDevice;
	List<StepCountDailyData> stepCountDailyData;
	
	
	public WearableDevice getWearableDevice() {
		return wearableDevice;
	}
	
	public void setWearableDevice(WearableDevice wearableDevice) {
		this.wearableDevice = wearableDevice;
	}

	public List<StepCountDailyData> getStepCountDailyData() {
		return stepCountDailyData;
	}

	public void setStepCountDailyData(List<StepCountDailyData> stepCountDailyData) {
		this.stepCountDailyData = stepCountDailyData;
	}
	
	
	
	
}
