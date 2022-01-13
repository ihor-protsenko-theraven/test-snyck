package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ExternalAPIGetMonitoringResponse;
import com.essence.hc.model.ExternalAPIResidentMonitoring;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIGetMonitoringResponsePrsr implements IParser<ExternalAPIGetMonitoringResponse> {

	private List<ExternalAPIResidentMonitoringPrsr> externalAPIResidentMonitoringList;
	private int response;
	private String responseDescription;
	private String message;

	public ExternalAPIGetMonitoringResponsePrsr() {
	}

	@Override
	public ExternalAPIGetMonitoringResponse parse() {

		ExternalAPIGetMonitoringResponse externalAPIGetMonitoringResponse = new ExternalAPIGetMonitoringResponse();

		try {
			List<ExternalAPIResidentMonitoring> residentMonitoringList = new ArrayList<>();

			if (externalAPIResidentMonitoringList != null) {
				for(ExternalAPIResidentMonitoringPrsr i: externalAPIResidentMonitoringList){
					residentMonitoringList.add(i.parse());
				}
			}
			
			externalAPIGetMonitoringResponse.setExternalAPIResidentMonitoringList(residentMonitoringList);
			externalAPIGetMonitoringResponse.setResponse(response);
			externalAPIGetMonitoringResponse.setResponseDescription(responseDescription);
			externalAPIGetMonitoringResponse.setMessage(message);
		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		return externalAPIGetMonitoringResponse;
	}
	
	@JsonProperty("residentMonitoringList")
	public List<ExternalAPIResidentMonitoringPrsr> getExternalAPIResidentMonitoringList() {
		return externalAPIResidentMonitoringList;
	}

	@JsonProperty("residentMonitoringList")
	public void setExternalAPIResidentMonitoringList(
			List<ExternalAPIResidentMonitoringPrsr> externalAPIResidentMonitoringList) {
		this.externalAPIResidentMonitoringList = externalAPIResidentMonitoringList;
	}

	@JsonProperty("Response")
	public int getResponse() {
		return response;
	}

	@JsonProperty("Response")
	public void setResponse(int response) {
		this.response = response;
	}

	@JsonProperty("ResponseDescription")
	public String getResponseDescription() {
		return responseDescription;
	}
	
	@JsonProperty("ResponseDescription")
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	
	@JsonProperty("Message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("Message")
	public void setMessage(String message) {
		this.message = message;
	}
	
}
