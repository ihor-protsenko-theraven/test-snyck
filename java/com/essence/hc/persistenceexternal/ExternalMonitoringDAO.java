package com.essence.hc.persistenceexternal;

import java.util.List;

import com.essence.hc.model.ExternalAPIGetMonitoringResponse;
import com.essence.hc.model.ItemsFilter;
import com.essence.hc.model.Order;
import com.essence.hc.model.Event.EventType;
import com.essence.hc.model.Vendor.ServiceType;

public interface ExternalMonitoringDAO 
{
	/**
	 * Get residentMonitoring list
	 * @param enableActiveService
	 * @param serviceTypes
	 * @param eventTypes
	 * @param itemsFilter
	 * @param orders
	 * @return
	 */
	public ExternalAPIGetMonitoringResponse getMonitoringInfo(Boolean enableActiveService, String[] panelTypes,
			ServiceType[] serviceTypes, EventType[] eventTypes, ItemsFilter itemsFilter, List<Order> orders);
}
