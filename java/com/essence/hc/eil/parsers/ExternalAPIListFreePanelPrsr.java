package com.essence.hc.eil.parsers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.FreePanel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIListFreePanelPrsr implements IParser<List<FreePanel>> {
	
	private String response;
	private String responseDescription;
	private String message;
	private List<FreePanel> panels;

	@Override
	public List<FreePanel> parse() {
		return panels;
	}
	
	/* Getters and setters are duplicated because this parser is used for both APIs. 
	 * The response has the same form, but property names are different.
	 */
	
	@JsonProperty("Response")
	public String getResponse() {
		return response;
	}

	@JsonProperty("Response")
	public void setResponse(String response) {
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

	public List<FreePanel> getPanels() {
		return panels;
	}

	public void setPanels(List<FreePanel> panels) {
		this.panels = panels;
	}
}
