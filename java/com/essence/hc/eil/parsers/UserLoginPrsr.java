package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;
import com.essence.hc.model.User;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginPrsr implements IParser<User> {
	
	private int iCrGvrId;
	private String sUsrType;
	private String[] iPatientId;
	private String[] iCpId;
	private String[] sfullName;
	private boolean[] iUsrAttrs;
	private int iVendorId;
	private String sTelfPolice;
	private int iLanguageId;
	private boolean mobileModeOnDesktop;
	private String[] serviceTypes;
	
	protected final Logger logger = (Logger) LogManager.getLogger(UserLoginPrsr.class);
	
	public UserLoginPrsr() {
	}


	@Override
	public User parse() {
		User user = null;
		try {
			if (this.iCrGvrId > 0) {
				user = new User();
				user.setUserId(this.iCrGvrId);
				user.setVendorId(this.iVendorId);
				user.setTlfPolice(sTelfPolice);
				user.setLanguageId(iLanguageId);
				//user.setLanguage(Language.getLanguage(iLanguageId));
				user.setMobileModeOnDesktop(this.mobileModeOnDesktop);
				if (this.sUsrType != null && this.sUsrType.equalsIgnoreCase("Administrator")) {
					user.setUserType(UserType.ROLE_ADMIN);
				}else if (this.sUsrType != null && this.sUsrType.contains("Caregiver")){
					user.setUserType(UserType.ROLE_CAREGIVER);
				}
				if(iPatientId != null) {
					List<Patient> pList = new ArrayList<Patient>();
					for(int i=0; i < iPatientId.length; i++) {
						Patient p = new Patient();
						p.setUserId(Integer.parseInt(iPatientId[i]));
//						p.setCpId(iCpId[i]);
						p.setFirstName(sfullName[i]);
						p.setServiceType(Vendor.ServiceType.fromString(serviceTypes[i]));
						pList.add(p);
					}
					user.setPatients(pList);
				}
			}
		}catch(Exception ex) {
			logger.error("Error parsing UserLogin");
			throw new ParseException(ex,"Unexpected parse error");
		}
		return user;
	}
	
	
	public int getiCrGvrId() {
		return iCrGvrId;
	}


	public void setiCrGvrId(int iCrGvrId) {
		this.iCrGvrId = iCrGvrId;
	}


	public String getsUsrType() {
		return sUsrType;
	}


	public void setsUsrType(String sUsrType) {
		this.sUsrType = sUsrType;
	}


	public String[] getiPatientId() {
		return iPatientId;
	}


	public void setiPatientId(String[] iPatientId) {
		this.iPatientId = iPatientId;
	}


	public String[] getSfullName() {
		return sfullName;
	}


	public void setSfullName(String[] sfullName) {
		this.sfullName = sfullName;
	}


	public boolean[] getiUsrAttrs() {
		return iUsrAttrs;
	}


	public void setiUsrAttrs(boolean[] iUsrAttrs) {
		this.iUsrAttrs = iUsrAttrs;
	}


	public String[] getiCpId() {
		return iCpId;
	}


	public void setiCpId(String[] iCpId) {
		this.iCpId = iCpId;
	}


	public int getiVendorId() {
		return iVendorId;
	}


	public void setiVendorId(int iVendorId) {
		this.iVendorId = iVendorId;
	}

	@JsonProperty("TelfPolice")
	public String getsTelfPolice() {
		return sTelfPolice;
	}

	@JsonProperty("TelfPolice")
	public void setsTelfPolice(String sTelfPolice) {
		this.sTelfPolice = sTelfPolice;
	}
	
	@JsonProperty("LanguageId")
	public int getiLanguageId() {
		return iLanguageId;
	}
	@JsonProperty("LanguageId")
	public void setiLanguageId(int iLanguageId) {
		this.iLanguageId = iLanguageId;
	}


	public boolean getMobileModeOnDesktop() {
		return mobileModeOnDesktop;
	}


	public void setMobileModeOnDesktop(boolean mobileModeOnDesktop) {
		this.mobileModeOnDesktop = mobileModeOnDesktop;
	}

	@JsonProperty("PatientsServiceType")
	public String[] getServiceTypes() {
		return serviceTypes;
	}

	@JsonProperty("PatientsServiceType")
	public void setServiceTypes(String[] serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	
}
