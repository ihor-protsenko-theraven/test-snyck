/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oscar.canalejo
 *
 */
public class ActivityLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean patientAtHome;
	private ActivityDetection lastLocation;
	private List<ActivityDetection> activityDetections;
	private int activityLevelExpress; // The calculated activity level for Express Residents
	private List<StepCountAvailableReport> stepCountAvailableReport;
	
	public boolean isPatientAtHome() {
		return patientAtHome;
	}
	public void setPatientAtHome(boolean patientAtHome) {
		this.patientAtHome = patientAtHome;
	}
	
	public ActivityDetection getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(ActivityDetection lastLocation) {
		this.lastLocation = lastLocation;
	}
	public List<ActivityDetection> getActivityDetections() {
		return activityDetections;
	}
	public void setActivityDetections(List<ActivityDetection> activityDetections) {
		this.activityDetections = activityDetections;
	}
	
	public int getActivityLevelExpress() {
		return activityLevelExpress;
	}
	public void setActivityLevelExpress(int activityLevelExpress) {
		this.activityLevelExpress = activityLevelExpress;
	}

	/**
	 * Nested Class for Activity Detections
	 */
	public static class ActivityDetection implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String locationType;
		private String alias;
		private String description;
		private Date dateTimeStart;
		private Date dateTimeEnd;
		private float registeredLevel;
		private int requiredLevel;
		
		public Date getDateTimeStart() {
			return dateTimeStart;
		}
		public void setDateTimeStart(Date dateTimeStart) {
			this.dateTimeStart = dateTimeStart;
		}
		public Date getDateTimeEnd() {
			return dateTimeEnd;
		}
		public void setDateTimeEnd(Date dateTimeEnd) {
			this.dateTimeEnd = dateTimeEnd;
		}
		public float getRegisteredLevel() {
			return registeredLevel;
		}
		public void setRegisteredLevel(float d) {
			this.registeredLevel = d;
		}
		public int getRequiredLevel() {
			return requiredLevel;
		}
		public void setRequiredLevel(int requiredLevel) {
			this.requiredLevel = requiredLevel;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getLocationType() {
			return locationType;
		}
		public void setLocationType(String locationType) {
			// Transformation to adapt express names to their correspondence in analytics, since the returned values are different, but we want the same messages and icons
			// FrontDoor is a special case, because when last location is FrontDoor for express we show the door icon, while FRONT_DOOR is considered an indication of out of home in analytics
			if ("Unknown".equals(locationType)) {
				//this.locationType = "UNKNOWN";
				this.locationType = "OTHER_ROOM";
			} else if ("FridgeDoor".equals(locationType)) {
				this.locationType = "FRIDGE_DOOR";
			} else if ("Bedroom".equals(locationType) || "BedSensor".equals(locationType) || "21".equals(locationType)) {
				this.locationType = "BEDROOM_SENSOR";
			} else if ("ToiletRoom".equals(locationType) || "Restroom".equals(locationType)) {
				this.locationType = "TOILET_ROOM_SENSOR";
			} else if ("Bathroom".equals(locationType)) {
				this.locationType = "BATHROOM";
			} else if ("LivingRoom".equals(locationType)) {
				this.locationType = "LIVING_ROOM";
			} else if ("DiningRoom".equals(locationType)) {
				this.locationType = "DINING_ROOM";
			} else {
				this.locationType = locationType;
			}
		}
	}

	public List<StepCountAvailableReport> getStepCountAvailableReport() {
		return stepCountAvailableReport;
	}
	public void setStepCountAvailableReport(List<StepCountAvailableReport> stepCountAvailableReport) {
		this.stepCountAvailableReport = stepCountAvailableReport;
	}

	
	
}
