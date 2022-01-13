package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.Installation;
import com.essence.hc.model.Patient;
import com.essence.hc.model.User.CaregiverType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PermittedAccountsPrsr implements IParser<List<Patient>> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private Map<String, List<ExternalAPIAccountPrsr>> accounts;

	@Override
	public List<Patient> parse() {
		List<Patient> result = new ArrayList<Patient>();
		for (Map.Entry<String, List<ExternalAPIAccountPrsr>> entry : accounts.entrySet()) {
			String caregiverTypeString = entry.getKey();
			CaregiverType caregiverType = null;
			if ("MasterCareGiver".equals(caregiverTypeString)) {
				caregiverType = CaregiverType.MASTER;
			} else if ("StandardCareGiver".equals(caregiverTypeString)) {
				caregiverType = CaregiverType.STANDARD;
			}
			for (ExternalAPIAccountPrsr account: entry.getValue()) {
				Patient resident = new Patient();
				Installation installation = account.parse();
				resident.setCaregiverType(caregiverType);
				resident.setInstallation(installation);
				
				// Data about the resident but included in the account in the external API
				resident.setAddress(account.getAddress());
				resident.setPhone(account.getHomePhone());
				
				result.add(resident);
			}
		}
		return result;
	}

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

	@JsonProperty("Value")
	public boolean isValue() {
		return value;
	}

	@JsonProperty("Value")
	public void setValue(boolean value) {
		this.value = value;
	}

	public Map<String, List<ExternalAPIAccountPrsr>> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, List<ExternalAPIAccountPrsr>> accounts) {
		this.accounts = accounts;
	}

}
