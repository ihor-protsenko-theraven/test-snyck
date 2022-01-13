package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.ExternalAPIAdditionalMobileData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIAdditionalMobileDataPrsr implements IParser<ExternalAPIAdditionalMobileData> {

	private String mobileDeviceId;
	private ExternalAPIPhoneNumberPrsr phoneNumber;

	public ExternalAPIAdditionalMobileDataPrsr() {
	}

	@Override
	public ExternalAPIAdditionalMobileData parse() {
		ExternalAPIAdditionalMobileData data = new ExternalAPIAdditionalMobileData();
		data.setMobileDeviceId(mobileDeviceId);
		data.setPhoneNumber(phoneNumber.parse());
		return data;
	}

	@JsonProperty("mobileDeviceId")
	public String getMobileDeviceId() {
		return mobileDeviceId;
	}

	@JsonProperty("mobileDeviceId")
	public void setMobileDeviceId(String mobileDeviceId) {
		this.mobileDeviceId = mobileDeviceId;
	}

	@JsonProperty("mobilePhoneNumber")
	public ExternalAPIPhoneNumberPrsr getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("mobilePhoneNumber")
	public void setPhoneNumber(ExternalAPIPhoneNumberPrsr phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
