package com.essence.hc.model;

import java.util.List;

import com.essence.hc.model.Activity.ActivityType;

public class ExternalAPIDevice {

	public static enum DeviceEPAStatus {
		NOT_PAIRED(0), PAIRED_AND_RESPONSIVE(1), PAIRED_AND_UNRESPONSIVE(2), UNKNOWN(-1);

		private int id;

		DeviceEPAStatus(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static DeviceEPAStatus byId(int id) {
			for (DeviceEPAStatus epaStatus : DeviceEPAStatus.values()) {
				if (epaStatus.getId() == id)
					return epaStatus;
			}

			return UNKNOWN;
		}
	}

	private int deviceId;
	private String deviceIdentifier;
	private ActivityType activityType;
	private String label;
	private String serial;
	private String deviceType;
	private int deviceFamily;
	private DeviceEPAStatus mobilePairingStatus;
	private ExternalAPIAdditionalMobileData addData;
	private boolean isIPD;
	public String correlatedDeviceIdentifier; // Correlated Device. (since v1.2, for Bathroom-Restroom device
												// correlation
	// feature)
	public ExternalAPIDevice correlatedDev;
	private List<DeviceAssociation> deviceAssociations;
	private List<DeviceAttribute> attributes;
	
	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}

	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getDeviceFamily() {
		return deviceFamily;
	}

	public void setDeviceFamily(int deviceFamily) {
		this.deviceFamily = deviceFamily;
	}

	public boolean getIsIPD() {
		return isIPD;
	}

	public void setIsIPD(boolean isIPD) {
		this.isIPD = isIPD;
	}

	public DeviceEPAStatus getMobilePairingStatus() {
		return this.mobilePairingStatus;
	}

	public void calculateMobilePairingStatus() {
		// Calculated according VLH 2.5.5 information (7.4.3.6)
		if ((addData != null) && (addData.getMobileDeviceId().length() > 0)) {
			mobilePairingStatus = DeviceEPAStatus.PAIRED_AND_RESPONSIVE;
		} else
			mobilePairingStatus = DeviceEPAStatus.NOT_PAIRED;
	}

	public boolean calculateIsIPD() {
		if (deviceType.compareTo("CameraMotionDetector") == 0) {
			return true;
		} else
			return false;
	}

	public ExternalAPIAdditionalMobileData getAddData() {
		return addData;
	}

	public void setAddData(ExternalAPIAdditionalMobileData addData) {
		this.addData = addData;
	}

	public ExternalAPIDevice getCorrelatedDev() {
		return correlatedDev;
	}

	public void setCorrelatedDev(ExternalAPIDevice correlatedDev) {
		this.correlatedDev = correlatedDev;
	}

	public String getCorrelatedDeviceIdentifier() {
		return correlatedDeviceIdentifier;
	}

	public void setCorrelatedDeviceIdentifier(String correlatedDeviceIdentifier) {
		this.correlatedDeviceIdentifier = correlatedDeviceIdentifier;
	}

	public List<DeviceAssociation> getDeviceAssociations() {
		return deviceAssociations;
	}

	public void setDeviceAssociations(List<DeviceAssociation> deviceAssociations) {
		this.deviceAssociations = deviceAssociations;
	}

	public List<DeviceAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<DeviceAttribute> attributes) {
		this.attributes = attributes;
	}
}
