package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.ExternalAPIPhoneNumber;

public class ExternalAPIPhoneNumberPrsr implements IParser<ExternalAPIPhoneNumber> {

	String countryCode;
	String phoneNumber;
	
	public ExternalAPIPhoneNumberPrsr() {
	}
	
	@Override
	public ExternalAPIPhoneNumber parse() {
		ExternalAPIPhoneNumber phone = new ExternalAPIPhoneNumber();
		phone.setCountryCode(countryCode);
		phone.setPhoneNumber(phoneNumber);

		return phone;
	}

	@JsonProperty("countryCode")
	public String getCountryCode() {
		return countryCode;
	}

	@JsonProperty("countryCode")
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@JsonProperty("phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("phoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
