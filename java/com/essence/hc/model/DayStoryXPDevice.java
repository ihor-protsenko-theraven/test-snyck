package com.essence.hc.model;

import java.util.List;

import com.essence.hc.model.Activity.ActivityType;

public class DayStoryXPDevice {

	private int deviceId;
	private ActivityType activityType;
	private String deviceLabel;
	
	private List<Activity> posaData;
	private List<Activity> activityData;
	
	
	
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public ActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	public String getDeviceLabel() {
		return deviceLabel;
	}
	public void setDeviceLabel(String deviceLabel) {
		this.deviceLabel = deviceLabel;
	}
	public List<Activity> getPosaData() {
		return posaData;
	}
	public void setPosaData(List<Activity> posaData) {
		this.posaData = posaData;
	}
	public List<Activity> getActivityData() {
		return activityData;
	}
	public void setActivityData(List<Activity> activityData) {
		this.activityData = activityData;
	}
	
	
}
