package com.essence.hc.eil.parsers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Device;
import com.essence.hc.model.Device.DeviceState;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DayStoryXPActivityPrsr implements IParser<Activity> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String id;
	private String cpId;
	private String deviceId;
	private Date started;
	private Date ended;
	private String duration;
	private String activityType;
	private String status;
	private String activityGroupId;
	private String activitySignalId;
	private String deviceLabel;

	/**
	 * Activity Type Names which are returned from the BackEnd API
	 * 0x00 Unknown (aka “Other Room”)
	 * 0x01 Fridge door
	 * 0x02 Entrance door (front door, backdoor, etc)
	 * 0x03 Bedroom
	 * 0x04 Toilet room
	 * 0x05 Bathroom
	 * 0x06 Living room
	 * 0x07 Dining room / Kitchen
	 * 0x08 Canteen drawer (FFU) (is likely that we'll never have to deal with this type)
	 */
	public static final Map<String, ActivityType> ACTIVITY_NAMES = new HashMap<String, ActivityType>();
	static{
		ACTIVITY_NAMES.put("Unknown",ActivityType.OTHER_ROOM);
		ACTIVITY_NAMES.put("FridgeDoor", ActivityType.FRIDGE_DOOR);
		ACTIVITY_NAMES.put("FrontDoor", ActivityType.FRONT_DOOR);
		ACTIVITY_NAMES.put("Bedroom",ActivityType.BEDROOM_SENSOR);
		ACTIVITY_NAMES.put("ToiletRoom", ActivityType.TOILET_ROOM_SENSOR);
		ACTIVITY_NAMES.put("Restroom", ActivityType.TOILET_ROOM_SENSOR);
		ACTIVITY_NAMES.put("Bathroom", ActivityType.BATHROOM_SENSOR);
		ACTIVITY_NAMES.put("LivingRoom", ActivityType.LIVING_ROOM);
		ACTIVITY_NAMES.put("DiningRoom", ActivityType.DINING_ROOM);
//		ACTIVITY_NAMES.put("Canteen drawer", ActivityType.xxxxxx);
	};
	
	@Override
	public Activity parse() {
		

		Activity act = new Activity();
			
		try{
			act.setId(id);
			act.setOriginalStartTime(started);
			act.setOriginalEndTime(ended);
			if(ACTIVITY_NAMES.containsKey(activityType)) {
				act.setType(ACTIVITY_NAMES.get(activityType));
			}
			Device actDevice = new Device();
			actDevice.setId(String.valueOf(deviceId));
			actDevice.setLabel(deviceLabel);
			if (activitySignalId != null) {
				actDevice.setState((Integer.parseInt(activitySignalId) == 1)?DeviceState.ON:DeviceState.OFF);				
			}
			act.setDevice(actDevice);
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return act;
	}


	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("CPId")
	public String getCpId() {
		return cpId;
	}

	@JsonProperty("CPId")
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	@JsonProperty("DeviceId")
	public String getDeviceId() {
		return deviceId;
	}

	@JsonProperty("DeviceId")
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonProperty("Started")
	public Date getStarted() {
		return started;
	}

	@JsonProperty("Started")
	public void setStarted(Date started) {
		this.started = started;
	}

	@JsonProperty("Ended")
	public Date getEnded() {
		return ended;
	}

	@JsonProperty("Ended")
	public void setEnded(Date ended) {
		this.ended = ended;
	}

	@JsonProperty("Duration")
	public String getDuration() {
		return duration;
	}

	@JsonProperty("Duration")
	public void setDuration(String duration) {
		this.duration = duration;
	}

	@JsonProperty("ActivityType")
	public String getActivityType() {
		return activityType;
	}

	@JsonProperty("ActivityType")
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@JsonProperty("Status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("ActivityGroupId")
	public String getActivityGroupId() {
		return activityGroupId;
	}

	@JsonProperty("ActivityGroupId")
	public void setActivityGroupId(String activityGroupId) {
		this.activityGroupId = activityGroupId;
	}

	@JsonProperty("ActivitySignalId")
	public String getActivitySignalId() {
		return activitySignalId;
	}

	@JsonProperty("ActivitySignalId")
	public void setActivitySignalId(String activitySignalId) {
		this.activitySignalId = activitySignalId;
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
