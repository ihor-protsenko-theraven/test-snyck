package com.essence.hc.model;

import com.essence.hc.model.User.CaregiverType;

public class UserLimitation {	
	
	private CaregiverType caregiverType;
	private int maxValue;
	
	public CaregiverType getCaregiverType() {
		return caregiverType;
	}
	public void setCaregiverType(CaregiverType caregiverType) {
		this.caregiverType = caregiverType;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

}
