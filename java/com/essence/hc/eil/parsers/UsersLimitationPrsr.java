package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.UserLimitation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersLimitationPrsr implements IParser<UserLimitation> {
	
	private int caregiverTypeCode;
	private int maximumValue;

	@Override
	public UserLimitation parse() {
		
		UserLimitation ul = new UserLimitation();
		
		if (caregiverTypeCode == 0) {
			ul.setCaregiverType(CaregiverType.STANDARD);
		} else if (caregiverTypeCode == 1) {
			ul.setCaregiverType(CaregiverType.MASTER);
		} 
		
		ul.setMaxValue(this.maximumValue);
		return ul;
	}

	@JsonProperty("CareGiverType")
	public int getCaregiverTypeCode() {
		return caregiverTypeCode;
	}

	@JsonProperty("CareGiverType")
	public void setCaregiverTypeCode(int caregiverTypeCode) {
		this.caregiverTypeCode = caregiverTypeCode;
	}

	@JsonProperty("MaxValue")
	public int getMaximumValue() {
		return maximumValue;
	}

	@JsonProperty("MaxValue")
	public void setMaximumValue(int maximumValue) {
		this.maximumValue = maximumValue;
	}

}
