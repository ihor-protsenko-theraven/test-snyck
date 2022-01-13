package com.essence.hc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.model.ExternalAPIGetMonitoringResponse;
import com.essence.hc.model.ExternalAPIResidentMonitoring;
import com.essence.hc.model.ItemsFilter;
import com.essence.hc.model.Order;
import com.essence.hc.model.Report.ReportType;
import com.essence.hc.model.Event.EventType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistenceexternal.ExternalMonitoringDAO;
import com.essence.hc.service.MonitoringService;

public class MonitoringServiceImpl implements MonitoringService {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExternalMonitoringDAO externalMonitoringDao;

	@Override
	public List<ExternalAPIResidentMonitoring> getMonitoringInfo(Boolean enableActiveService, String[] panelTypes, 
			ServiceType[] serviceTypes, EventType[] eventTypes, ItemsFilter itemsFilter, List<Order> orders) {
		
		ExternalAPIGetMonitoringResponse monitoringResponse = 
				externalMonitoringDao.getMonitoringInfo(enableActiveService, panelTypes, serviceTypes, eventTypes, itemsFilter, orders);
		List<ExternalAPIResidentMonitoring> monitoringInfo = monitoringResponse.getExternalAPIResidentMonitoringList();
		return setAvailableReports(monitoringInfo);
	}
	
	private List<ExternalAPIResidentMonitoring> setAvailableReports(List<ExternalAPIResidentMonitoring> monitoringInfo) {
		
		for(ExternalAPIResidentMonitoring monitoring : monitoringInfo) {
			monitoring.setAvailableReports(monitoring.obtainDefaultAvailableReports());
			if(monitoring.hasStepCountData()) {
				monitoring.addAvailableReport(ReportType.STEP_COUNT);
			}
		}
		
		return monitoringInfo;
	}
}
