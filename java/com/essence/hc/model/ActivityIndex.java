package com.essence.hc.model;

import java.util.Date;
import java.util.List;

/**
 * @author oscar.canalejo
 *
 */
public class ActivityIndex {

	private Date date;
	private int index;
	private List<DeviceIndex> deviceIndexData;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<DeviceIndex> getDeviceIndexData() {
		return deviceIndexData;
	}
	public void setDeviceIndexData(List<DeviceIndex> deviceIndexData) {
		this.deviceIndexData = deviceIndexData;
	}
	
}
