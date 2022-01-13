package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.DayStoryXPDevice;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DayStoryXPDevicePrsr implements IParser<DayStoryXPDevice> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private int deviceId;
	private String activityType;
	private String deviceLabel;
	
	private List<DayStoryXPActivityPrsr> posaData;
	private List<DayStoryXPActivityPrsr> activityData;
	
	@Override
	public DayStoryXPDevice parse() {

		DayStoryXPDevice dev = new DayStoryXPDevice();
		List<Activity> posaActivities;
		List<Activity> rawActivities;
		
		try {
			dev.setDeviceId(deviceId);
			if(DayStoryXPActivityPrsr.ACTIVITY_NAMES.containsKey(activityType)) {
				dev.setActivityType(DayStoryXPActivityPrsr.ACTIVITY_NAMES.get(activityType));
			}
			dev.setDeviceLabel(deviceLabel);
			/*
			 * POSA Data
			 */
			if(posaData != null) {
				posaActivities = new ArrayList<>();
				for(DayStoryXPActivityPrsr ds: posaData){
					posaActivities.add((Activity)ds.parse());
				}
				dev.setPosaData(posaActivities);
			}
			/*
			 * Activity Data (Door open/close detections)
			 */
			if(activityData != null) {
				rawActivities = new ArrayList<>();
				for(DayStoryXPActivityPrsr ds: activityData){
					rawActivities.add((Activity)ds.parse());
				}
				dev.setActivityData(rawActivities);
			}
				
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return  dev;
	}

	
	@JsonProperty("PoSAData")
	public List<DayStoryXPActivityPrsr> getPosaData() {
		return posaData;
	}

	@JsonProperty("PoSAData")
	public void setPosaData(List<DayStoryXPActivityPrsr> posaData) {
		this.posaData = posaData;
	}

	@JsonProperty("ActivityData")
	public List<DayStoryXPActivityPrsr> getActivityData() {
		return activityData;
	}

	@JsonProperty("ActivityData")
	public void setActivityData(List<DayStoryXPActivityPrsr> activityData) {
		this.activityData = activityData;
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

	@JsonProperty("DeviceLabel")
	public String getDeviceLabel() {
		return deviceLabel;
	}

	@JsonProperty("DeviceLabel")
	public void setDeviceLabel(String deviceLabel) {
		this.deviceLabel = deviceLabel;
	}
	
	
}
