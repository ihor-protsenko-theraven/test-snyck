package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ResponseStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIResponseStatusPrsr implements IParser<ResponseStatus> {
	
	private String response;
	private String responseDescription;
	private String value;
	private String message;

	@Override
	public ResponseStatus parse() {
		ResponseStatus responseStatus = new ResponseStatus();
		try {	
 			responseStatus.setMessageErr(responseDescription);
			responseStatus.setMessage(message);
			responseStatus.setNumErr(Integer.parseInt(response));
			if ("OK".equals(responseDescription))
				responseStatus.setOK(true);
			else
				responseStatus.setOK(false);
			responseStatus.setValue(value);
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
		
		return responseStatus;
	}

	/**
	 * @return the response
	 */
	@JsonProperty("Response")
	public String getResponse() {
		return response;
	}


	/**
	 * @param response the response to set
	 */
	@JsonProperty("Response")
	public void setResponse(String response) {
		this.response = response;
	}


	/**
	 * @return the responseDescription
	 */
	@JsonProperty("ResponseDescription")
	public String getResponseDescription() {
		return responseDescription;
	}


	/**
	 * @param responseDescription the responseDescription to set
	 */
	@JsonProperty("ResponseDescription")
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	@JsonProperty("Value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@JsonProperty("Message")
	public String getMessage() {
		return this.message;
	}
	
	@JsonProperty("Message")
	public void setMessage(String message) {
		this.message = message;
	}

}
