package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ActivityIndex;
import com.essence.hc.model.DeviceIndex;
import com.essence.hc.util.Util;

/**
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityIndexPrsr implements IParser<ActivityIndex> {

	private String date;
	private String index;
	private List<DeviceIndexPrsr> deviceIndexData;

	
	@Override
	public ActivityIndex parse() {
		try {
			ActivityIndex activityIndex = new ActivityIndex();
			activityIndex.setDate(Util.parseDate(date, Util.DATEFORMAT_INPUT));
			activityIndex.setIndex(Integer.parseInt(index));
			if(deviceIndexData != null) {
				List<DeviceIndex> deviceIndexList = new ArrayList<>();
				for (DeviceIndexPrsr deviceIndexPrsr : deviceIndexData) {
					deviceIndexList.add((DeviceIndex)deviceIndexPrsr.parse());
				}
				activityIndex.setDeviceIndexData(deviceIndexList);
			}
			
			return activityIndex;
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
	}

	
	@JsonProperty("Date")
	public String getDate() {
		return date;
	}
	@JsonProperty("Date")
	public void setDate(String date) {
		this.date = date;
	}
	@JsonProperty("Index")
	public String getIndex() {
		return index;
	}
	@JsonProperty("Index")
	public void setIndex(String index) {
		this.index = index;
	}
	@JsonProperty("DevicesIndexData")
	public List<DeviceIndexPrsr> getDeviceIndexData() {
		return deviceIndexData;
	}
	@JsonProperty("DevicesIndexData")
	public void setDeviceIndexData(List<DeviceIndexPrsr> deviceIndexData) {
		this.deviceIndexData = deviceIndexData;
	}
	
}
