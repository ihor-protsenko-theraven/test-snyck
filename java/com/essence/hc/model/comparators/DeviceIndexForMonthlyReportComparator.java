package com.essence.hc.model.comparators;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.model.DeviceIndex;

/**
 * ad-hoc comparator to sort monthly report activities in the required order, which is different from id or priority
 * @author ruben.sanchez
 *
 */
public class DeviceIndexForMonthlyReportComparator implements Comparator<DeviceIndex> {
	
//	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public int compare(DeviceIndex di1, DeviceIndex di2) {
		return monthlyReportPripority(di1).compareTo(monthlyReportPripority(di2));
	}
	
	private Integer monthlyReportPripority(DeviceIndex di) {
		int priority = di.getDeviceId() + 100;
		if (di.getActivityType() != null) {
			switch (di.getActivityType()) {
				case DINING_ROOM:
					priority = 1;
					break;
				case TOILET_ROOM_SENSOR:
					priority = 2;
					break;
				case BATHROOM_SENSOR:
					priority = 3;
					break;
				case LIVING_ROOM:
					priority = 4;
					break;
				case OTHER_ROOM:
					priority = 5;
					break;
				case BEDROOM_SENSOR:
					priority = 6;
					break;
			}
		}
		return new Integer(priority);
	}

}
