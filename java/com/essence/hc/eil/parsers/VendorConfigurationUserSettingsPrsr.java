package com.essence.hc.eil.parsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.VendorConfigurationUserSettings;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.UserLimitation;
import com.essence.hc.model.Vendor.ServiceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorConfigurationUserSettingsPrsr implements IParser<VendorConfigurationUserSettings> {
	
	private int serviceTypeCode;
	private List<UsersLimitationPrsr> userLimitationList;
	
	@Override
	public VendorConfigurationUserSettings parse() {
		
		VendorConfigurationUserSettings vcus = new VendorConfigurationUserSettings();
		
		if (serviceTypeCode == 2) {
			vcus.setServiceType(ServiceType.EXPRESS);
		} else if (serviceTypeCode == 1) {
			vcus.setServiceType(ServiceType.ANALYTICS);
		}
		
		Map<CaregiverType, Integer> userLimitationMap = new HashMap<CaregiverType, Integer>();
		if(userLimitationList != null) {
			for(UsersLimitationPrsr item: userLimitationList) {
				UserLimitation ul = item.parse();
				if (ul.getCaregiverType() != null) {
					userLimitationMap.put(ul.getCaregiverType(), new Integer(ul.getMaxValue()));
				}
			}
		}
		vcus.setUserLimitation(userLimitationMap);
		
		return vcus;
	}

	@JsonProperty("ServiceType")
	public int getServiceTypeCode() {
		return serviceTypeCode;
	}

	@JsonProperty("ServiceType")
	public void setsServiceTypeCode(int serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	@JsonProperty("UserLimitation")
	public List<UsersLimitationPrsr> getUserLimitationList() {
		return userLimitationList;
	}

	@JsonProperty("UserLimitation")
	public void setUserLimitationList(List<UsersLimitationPrsr> userLimitationList) {
		this.userLimitationList = userLimitationList;
	}
}
