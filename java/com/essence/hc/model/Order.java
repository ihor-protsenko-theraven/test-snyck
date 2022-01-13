package com.essence.hc.model;

import org.springframework.util.StringUtils;

public class Order {
	public static enum OrderByType { 
		NONE("None"), GENERAL_ID("GeneralId"), DATE_TIME("DateTime"), FIRST_NAME("FirstName"), 
		LAST_LOCATION("LastLocation"), STATUS("Status"), ACCOUNT_NUMBER("AccountNumber"), 
		SECURITY_LEVEL_ID("SecurityLevelId"), PANEL_TYPE("PanelType"), SERVICE_TYPE("ServiceType"), 
		ENABLE_ACTIVE_SERVICE("EnableActiveService"), SERVICE_PACKAGE("ServicePackage"); 
		
		private final String name;
		
		OrderByType(String name) { this.name = name; }
		
		public String getName() { return name; }
		
		public String toString() { return getName(); }
		
		public static OrderByType getOrderByType(String text) {
			if (StringUtils.hasText(text)) {
				for (OrderByType type : OrderByType.values()) {
					if (text.equalsIgnoreCase(type.name)) {
						return type;
					}
				}
			}
			return NONE;
		}
	}
	
	public static enum OrderDirection { 
		ASCENDING("Ascending"), DESCENDING("Descending"); 
		
		private final String name;
		
		OrderDirection(String name) { this.name = name; }
		
		public String getName() { return name; }
		
		public String toString() { return getName(); }
		
		public static OrderDirection getOrderDirection(Boolean isAscending) { 
			if (isAscending == null || isAscending) {
				return ASCENDING;
			} else {
				return DESCENDING;
			}
		}
	}

	private String orderBy;
	private String direction;
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
