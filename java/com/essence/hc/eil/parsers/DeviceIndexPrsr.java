package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.DeviceIndex;


/**
 * @author oscar.canalejo
 *
 */
public class DeviceIndexPrsr implements IParser<DeviceIndex> {

	private String activityType;
	private String index;
	private String deviceLabel;
	private int deviceId;

	@Override
	public DeviceIndex parse() {
		DeviceIndex deviceIndex = new DeviceIndex();
		deviceIndex.setDeviceId(deviceId);
		ActivityType aType = DayStoryXPActivityPrsr.ACTIVITY_NAMES.get(activityType);
		if(aType != null && aType != ActivityType.FRONT_DOOR) { //FrontDoor activity should not appear in Monthly report
			deviceIndex.setActivityType(DayStoryXPActivityPrsr.ACTIVITY_NAMES.get(activityType));
		}
		deviceIndex.setIndex(Integer.parseInt(index));
		deviceIndex.setDeviceLabel(deviceLabel);
		return deviceIndex;
	}

	@JsonProperty("Index")
	public String getIndex() {
		return index;
	}
	@JsonProperty("Index")
	public void setIndex(String index) {
		this.index = index;
	}

	@JsonProperty("DeviceLabel")
	public String getDeviceLabel() {
		return deviceLabel;
	}

	@JsonProperty("DeviceLabel")
	public void setDeviceLabel(String deviceLabel) {
		this.deviceLabel = deviceLabel;
	}
	
	@JsonProperty("DeviceId")
	public int getDeviceId() {
		return deviceId;
	}
	@JsonProperty("DeviceId")
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
	@JsonProperty("ActivityType")
	public String getActivityType() {
		return activityType;
	}
	
	@JsonProperty("ActivityType")
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}	
}
