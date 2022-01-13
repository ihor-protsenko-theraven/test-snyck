package com.essence.hc.model;

import java.util.Date;

/**
 * Container for periods of missing information, ie, no spreading of activity data due to missing information for any reason.
 *
 */
public class MissingInformation {	
	
	/**
	 * Cause for the missing data 
	 *
	 */
	public static enum Reason {
		lostTransmission, supervisionLoss, noCommunication, unknown;
	}
	
	private Reason reason;
	
	private String startTime; 
	private String endTime;
	
	private ActivityInformation previousActivity;
	

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public ActivityInformation getPreviousActivity() {
		return previousActivity;
	}

	public void setPreviousActivity(ActivityInformation previousActivity) {
		this.previousActivity = previousActivity;
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
	

}
