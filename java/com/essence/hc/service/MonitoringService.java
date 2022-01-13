package com.essence.hc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.essence.hc.model.ExternalAPIResidentMonitoring;
import com.essence.hc.model.ItemsFilter;
import com.essence.hc.model.Order;
import com.essence.hc.model.Event.EventType;
import com.essence.hc.model.Vendor.ServiceType;

@Service
public interface MonitoringService {
	
	/**
	 * Get residentMonitoring list
	 * @param enableActiveService
	 * @param serviceTypes
	 * @param eventTypes
	 * @param itemsFilter
	 * @param orders
	 * @return
	 */
	public List<ExternalAPIResidentMonitoring> getMonitoringInfo(Boolean enableActiveService, String[] panelTypes,
			ServiceType[] serviceTypes, EventType[] eventTypes, ItemsFilter itemsFilter, List<Order> orders);
}
