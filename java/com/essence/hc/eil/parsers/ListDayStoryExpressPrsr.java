package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.DayStoryXPDevice;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDayStoryExpressPrsr implements IParser<Map<String, List<?>>> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Date currentPanelTime;
	private List<DayStoryXPDevicePrsr> devices;
	private List<DayStoryXPActivityPrsr> outOfHomeData;
	
	@Override
	public Map<String, List<?>> parse() {

		List<DayStoryXPDevice> devicesList;
		List<Activity> outOfHomeActivities;
		Map<String, List<?>> dayStoryExpressMap = new HashMap<>();
		try {
			if(devices != null) {
				devicesList = new ArrayList<>();
				for(DayStoryXPDevicePrsr dev: devices){
					devicesList.add((DayStoryXPDevice)dev.parse());
				}
				dayStoryExpressMap.put("Devices", devicesList);
			}
			if(outOfHomeData != null) {
				outOfHomeActivities = new ArrayList<>();
				for(DayStoryXPActivityPrsr ds: outOfHomeData){
					outOfHomeActivities.add((Activity)ds.parse());
				}
				dayStoryExpressMap.put("OutOfHomeData", outOfHomeActivities);
			}
			if (getCurrentPanelTime() != null){
				List<Date> currentPanelTimeList = new ArrayList<Date>();
				currentPanelTimeList.add(getCurrentPanelTime());
				dayStoryExpressMap.put("CurrentPanelTime", currentPanelTimeList);
			}
				
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return  dayStoryExpressMap;
	}

	@JsonProperty("OutOfHomeData")
	public List<DayStoryXPActivityPrsr> getOutOfHomeData() {
		return outOfHomeData;
	}

	@JsonProperty("OutOfHomeData")
	public void setOutOfHomeData(List<DayStoryXPActivityPrsr> outOfHomeData) {
		this.outOfHomeData = outOfHomeData;
	}

	@JsonProperty("Devices")
	public List<DayStoryXPDevicePrsr> getDevices() {
		return devices;
	}
	@JsonProperty("Devices")
	public void setDevices(List<DayStoryXPDevicePrsr> devices) {
		this.devices = devices;
	}

	@JsonProperty("CurrentPanelTime")
	public Date getCurrentPanelTime() {
		return currentPanelTime;
	}

	@JsonProperty("CurrentPanelTime")
	public void setCurrentPanelTime(Date currentPanelTime) {
		this.currentPanelTime = currentPanelTime;
	}
	
}
