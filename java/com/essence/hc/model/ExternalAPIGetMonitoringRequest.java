package com.essence.hc.model;

import java.util.List;

public class ExternalAPIGetMonitoringRequest {
	
	private Boolean enableActiveService;
	private String[] panelTypeFilter;
	private String servicePackageFilter;
	private ItemsFilter itemsFilter;
	private List<Order> order;
	private List<String> eventTypeFilter;
	
	public Boolean isEnableActiveService() {
		return enableActiveService;
	}
	public void setEnableActiveService(Boolean enableActiveService) {
		this.enableActiveService = enableActiveService;
	}
	public String[] getPanelTypeFilter() {
		return panelTypeFilter;
	}
	public void setPanelTypeFilter(String[] panelTypeFilter) {
		this.panelTypeFilter = panelTypeFilter;
	}
	public String getServicePackageFilter() {
		return servicePackageFilter;
	}
	public void setServicePackageFilter(String servicePackageFilter) {
		this.servicePackageFilter = servicePackageFilter;
	}
	public ItemsFilter getItemsFilter() {
		return itemsFilter;
	}
	public void setItemsFilter(ItemsFilter itemsFilter) {
		this.itemsFilter = itemsFilter;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
	public List<String> getEventTypeFilter() {
		return eventTypeFilter;
	}
	public void setEventTypeFilter(List<String> eventTypeFilter) {
		this.eventTypeFilter = eventTypeFilter;
	}
	
}
