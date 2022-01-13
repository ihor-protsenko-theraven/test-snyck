package com.essence.hc.eil.parsers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.DeviceAssociation;
import com.essence.hc.model.DeviceAttribute;
import com.essence.hc.model.ExternalAPIDevice;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIDevicePrsr implements IParser<ExternalAPIDevice> {

	private int deviceId;
	private String deviceIdentifier;
	private String activityType;
	private String label;
	private String serial;
	private ExternalAPIAdditionalMobileDataPrsr addData;
	private String deviceType;
	private int deviceFamily;
	public String correlatedDevIdentifier;
	public List<DeviceAssociation> deviceAssociations;
	public List<DeviceAttribute> attributes;

	public ExternalAPIDevicePrsr() {
	}

	private ActivityType ReturnEnumValue(String activity) {

		switch (activity) {
		case "OtherRoom":
			return ActivityType.OTHER_ROOM;
		case "FridgeDoor":
			return ActivityType.FRIDGE_DOOR;
		case "FrontDoor":
			return ActivityType.FRONT_DOOR;
		case "BedroomSensor":
			return ActivityType.BEDROOM_SENSOR;
		case "BedSensor":
			return ActivityType.BED_SENSOR;
		case "ToiletRoomSensor":
			return ActivityType.TOILET_ROOM_SENSOR;
		case "BathroomSensor":
			return ActivityType.BATHROOM_SENSOR;
		case "LivingRoom":
			return ActivityType.LIVING_ROOM;
		case "DiningRoom":
			return ActivityType.DINING_ROOM;
		case "SmokeDetector":
			return ActivityType.SMOKE_DETECTOR;
		case "GasSensor":
			return ActivityType.GAS_SENSOR;
		case "BleDevice":
			return ActivityType.BLE_DEVICE;
		case "WaterLeakage":
			return ActivityType.WATER_LEAKAGE;
		case "EP":
			return ActivityType.EP;
		case "SPBP":
			return ActivityType.SPBP;
		case "VPD":
			return ActivityType.VPD;
		case "EPA":
			return ActivityType.EPA;
		case "EPPlus":
			return ActivityType.EPPlus;
		case "BathroomCombined":
			return ActivityType.BATHROOM_COMBINED;
		case "FallDetector":
			return ActivityType.FALL_DETECTOR;
		case "LostCommunication":
			return ActivityType.LOST_COMMUNICATION;
		case "CarbonMonoxideDetector":
			return ActivityType.CARBON_MONOXIDE_DETECTOR;
		case "EpilepsyDetector":
			return ActivityType.EPILEPSY_DETECTOR;
		case "EnuresisDetector":
			return ActivityType.ENURESIS_DETECTOR;
		case "Undefined":
			return ActivityType.UNDEFINED;
		}
		// Error: We stablish it now as Undefined
		return ActivityType.UNDEFINED;

	}

	@Override
	public ExternalAPIDevice parse() {
		// logger.info("\nParsing " + getClass() + "...\n");

		ExternalAPIDevice device = new ExternalAPIDevice();

		try {
			device.setActivityType(ReturnEnumValue(activityType));
			device.setDeviceFamily(deviceFamily);
			device.setDeviceId(deviceId);
			device.setDeviceIdentifier(deviceIdentifier);
			device.setDeviceType(deviceType);
			device.setLabel(label);
			device.setSerial(serial);
			// 2.5.5 versions calculates if is IPD and Mobile Pairing Status
			// They are not part anymore of the API response information.
			device.setIsIPD(device.calculateIsIPD());
			
			if (addData != null) {
				device.setAddData(addData.parse());
			}
			device.calculateMobilePairingStatus();
			device.setCorrelatedDeviceIdentifier(correlatedDevIdentifier);
			device.setDeviceAssociations(deviceAssociations);
			device.setAttributes(attributes);

		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		return device;
	}

	@JsonProperty("deviceId")
	public int getDeviceId() {
		return this.deviceId;
	}

	@JsonProperty("deviceId")
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	@JsonProperty("deviceIdentifier")
	public String getDeviceIdentifier() {
		return this.deviceIdentifier;
	}

	@JsonProperty("deviceIdentifier")
	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}

	@JsonProperty("activityType")
	public String getActivityType() {
		return this.activityType;
	}

	@JsonProperty("activityType")
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@JsonProperty("label")
	public String getLabel() {
		return this.label;
	}

	@JsonProperty("label")
	public void setLabel(String label) {
		this.label = label.trim();
	}

	@JsonProperty("serial")
	public String getSerial() {
		return this.serial;
	}

	@JsonProperty("serial")
	public void setSerial(String serial) {
		this.serial = serial;
	}

	@JsonProperty("deviceType")
	public String getDeviceType() {
		return this.deviceType;
	}

	@JsonProperty("deviceType")
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@JsonProperty("deviceFamily")
	public int getDeviceFamily() {
		return this.deviceFamily;
	}

	@JsonProperty("deviceFamily")
	public void setDeviceFamily(int deviceFamily) {
		this.deviceFamily = deviceFamily;
	}

	@JsonProperty("additionalData")
	public ExternalAPIAdditionalMobileDataPrsr getAddData() {
		return addData;
	}

	@JsonProperty("additionalData")
	public void setAddData(ExternalAPIAdditionalMobileDataPrsr addData) {
		this.addData = addData;
	}

	@JsonProperty("relatedDeviceIdentifier")
	public String getCorrelatedDevIdentifier() {
		return correlatedDevIdentifier;
	}

	@JsonProperty("relatedDeviceIdentifier")
	public void setCorrelatedDevIdentifier(String correlatedDevIdentifier) {
		this.correlatedDevIdentifier = correlatedDevIdentifier;
	}

	@JsonProperty("deviceAssociations")
	public List<DeviceAssociation> getDeviceAssociations() {
		return deviceAssociations;
	}

	@JsonProperty("deviceAssociations")
	public void setDeviceAssociations(List<DeviceAssociation> deviceAssociations) {
		this.deviceAssociations = deviceAssociations;
	}

	@JsonProperty("attributes")
	public List<DeviceAttribute> getAttributes() {
		return attributes;
	}

	@JsonProperty("attributes")
	public void setAttributes(List<DeviceAttribute> attributes) {
		this.attributes = attributes;
	}

}
