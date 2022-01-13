package com.essence.hc.model;

import java.util.List;

public class ExternalAPIGetMonitoringResponse {
	
	private List<ExternalAPIResidentMonitoring> externalAPIResidentMonitoringList;
	private int response;
	private String responseDescription;
	private String message;
	
	public List<ExternalAPIResidentMonitoring> getExternalAPIResidentMonitoringList() {
		return externalAPIResidentMonitoringList;
	}
	public void setExternalAPIResidentMonitoringList(List<ExternalAPIResidentMonitoring> externalAPIResidentMonitoringList) {
		this.externalAPIResidentMonitoringList = externalAPIResidentMonitoringList;
	}
	public int getResponse() {
		return response;
	}
	public void setResponse(int response) {
		this.response = response;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
