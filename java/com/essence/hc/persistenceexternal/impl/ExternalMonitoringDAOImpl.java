package com.essence.hc.persistenceexternal.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.model.Event.EventType;
import com.essence.hc.model.ExternalAPIGetMonitoringRequest;
import com.essence.hc.model.ExternalAPIGetMonitoringResponse;
import com.essence.hc.model.ItemsFilter;
import com.essence.hc.model.Order;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalMonitoringDAO;
import com.essence.hc.util.Util;

public class ExternalMonitoringDAOImpl extends ExternalDAOImpl implements ExternalMonitoringDAO 
{
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public ExternalAPIGetMonitoringResponse getMonitoringInfo(Boolean enableActiveService, String[] panelTypes,
			ServiceType[] serviceTypes, EventType[] eventTypes, ItemsFilter itemsFilter, List<Order> orders) {
		
		ExternalAPIGetMonitoringRequest request = new ExternalAPIGetMonitoringRequest();
			
		request.setPanelTypeFilter(panelTypes);
		
		String servicePackagesFilter = Util.buildServicePackagesFilterStringFromServiceTypes(serviceTypes);
		if (org.springframework.util.StringUtils.hasText(servicePackagesFilter)) {
			request.setServicePackageFilter(servicePackagesFilter);
		}
		
		if (eventTypes != null) {
			List<String> eventTypeFilter = new ArrayList<>();
			for (EventType e: eventTypes) {
				eventTypeFilter.add(e.toString());
			}
			request.setEventTypeFilter(eventTypeFilter);
		}
		
		request.setEnableActiveService(enableActiveService);
		if (itemsFilter == null) 
		{
			// ESC17-5519: This is a workaround. We need to implement pagination on the same way than Analysis tab
			itemsFilter = new ItemsFilter();
			itemsFilter.setSkip(0);
			itemsFilter.setTake(10000);
		}
		request.setItemsFilter(itemsFilter);
		request.setOrder(orders);
		
		return performRequestRawBody(ExternalAPIGetMonitoringResponse.class, "external_get_monitoring", request);
	}
}
