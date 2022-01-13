package com.essence.hc.eil.parsers;

import com.essence.hc.model.StepCountDailyData;

public class ExternalAPIStepCountDailyDataPrsr implements IParser<StepCountDailyData>{

	String startTime;
	String endTime;
	int stepCount;
	
	@Override
	public StepCountDailyData parse() {
		StepCountDailyData stepCountDailyData = new StepCountDailyData();
		
		stepCountDailyData.setStartTime(startTime);
		stepCountDailyData.setEndTime(endTime);
		stepCountDailyData.setStepCount(stepCount);
		
		return stepCountDailyData;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getStepCount() {
		return stepCount;
	}

	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}

	

	
	
}
