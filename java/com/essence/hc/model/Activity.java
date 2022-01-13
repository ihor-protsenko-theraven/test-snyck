/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A kind of {@link Event} which represents a patient's 
 * habit or action monitored/recorded/processed by the system.
 * The Activity entity acts also as a superclass for all different 
 * types of activities defined.
 * 
 * @author oscar.canalejo
 *
 */
public class Activity extends Event implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//{LIVINGROOM, SLEEP, BATHROOM, MEAL, TOILET, NOT_AT_HOME}
//	public static enum ActivityType { LIVING_ROOM(6), BEDROOM_SENSOR(3), BATHROOM_SENSOR(5), 
//										FRIDGE_DOOR(1), TOILET_ROOM_SENSOR(4), FRONT_DOOR(2), 
//										DINING_ROOM(7), STUDY_ROOM(8),SMOKE_DETECTOR(9), WATER_LEAKAGE(10),
//								    	EP(11),SPBP(12) ;

	public static enum ActivityFamily{
		FAMILY_ID_ACTIVITY_SENSOR (1),
		FAMILY_ID_SAFETY_AND_SOS (2);
		
		private final int value;
		
		private ActivityFamily (int value){
			 this.value = value;
		}
		
		public int getValue(){
			 return value;
		}
	}
	
	public static enum ActivityType {
		
		FRIDGE_DOOR(1,3), 
		FRONT_DOOR(2,6), 
		BEDROOM_SENSOR(3,5),   
		TOILET_ROOM_SENSOR(4,1),
		BATHROOM_SENSOR(5,2), 
		LIVING_ROOM(6,7),
		DINING_ROOM(7,4), 
		OTHER_ROOM(0,13), 
		EP(10,10),
		EPPlus(14,14),
		EPA(13,13),
		VPD(12,12),
		SPBP(11,11),
		SMOKE_DETECTOR(8,8), 
		WATER_LEAKAGE(9,9),
		BATHROOM_COMBINED(20,20),
		LOST_COMMUNICATION(99,99),
		BED_SENSOR(21,21), 
		FALL_DETECTOR(201,201),
		GAS_SENSOR(15,15),
		BLE_DEVICE(202, 202),
		CARBON_MONOXIDE_DETECTOR(203,203),
		EPILEPSY_DETECTOR(204,204),
		ENURESIS_DETECTOR(205,205),
		UNDEFINED(-1,500);
		
		private final int id;
		private final int priority;
		ActivityType(int id, int priority) {
			this.id = id;
			this.priority = priority;
		}
		public int getId() {return id;}
		public int getPriority() {return priority;}
		
		// Needed for ESC17-6388:
		public static ActivityType[] getActivityProducingDevices() {
			ActivityType[] activityProducingDevices = { FRIDGE_DOOR, FRONT_DOOR, BEDROOM_SENSOR, TOILET_ROOM_SENSOR, 
					BATHROOM_SENSOR, LIVING_ROOM, DINING_ROOM, OTHER_ROOM };
			return activityProducingDevices;
		}
	}		
		
	
	private ActivityType type;
	private Date originalStartTime;	// real time at which the activity started (startTime is 00:00 if it started the day before)
	private Date originalEndTime; // real time at which the activity ended (startTime is 23:59 if it ended the day after)
	private Device device;
	private int firstCell; // number of cell in which the span starts in the report
	private int lastCell; // number of cell in which the span ends in the report (this cell excluded)
	private List<Activity> coincidentActivities; // other activities which have to be displayed inside this one
	private Activity containedIn; // activity inside which one this will be displayed 
	
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	@Override
	public String getName() {
		if (this.name == null && this.type != null)
			this.name = this.type.toString().toLowerCase();
		return super.getName();
	}
	public Date getOriginalStartTime() {
		return originalStartTime;
	}
	public void setOriginalStartTime(Date originalStartTime) {
		this.originalStartTime = originalStartTime;
	}
	public Date getOriginalEndTime() {
		return originalEndTime;
	}
	public void setOriginalEndTime(Date originalEndTime) {
		this.originalEndTime = originalEndTime;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public int getFirstCell() {
		return firstCell;
	}
	public void setFirstCell(int firstCell) {
		this.firstCell = firstCell;
	}
	public int getLastCell() {
		return lastCell;
	}
	public void setLastCell(int lastCell) {
		this.lastCell = lastCell;
	}
	public List<Activity> getCoincidentActivities() {
		return coincidentActivities;
	}
	public void setCoincidentActivities(List<Activity> coincidentActivities) {
		this.coincidentActivities = coincidentActivities;
	}
	public void addCoincidentActivity(Activity activity) {
		if (this.coincidentActivities == null) {
			this.coincidentActivities = new ArrayList<Activity>();
		}
		this.coincidentActivities.add(activity);
	}
	public Activity getContainedIn() {
		return containedIn;
	}
	public void setContainedIn(Activity containedIn) {
		this.containedIn = containedIn;
	}
}
