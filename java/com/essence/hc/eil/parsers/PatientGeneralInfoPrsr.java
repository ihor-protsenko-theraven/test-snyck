package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;
import com.essence.hc.model.User.UserGender;
import com.essence.hc.util.Util;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientGeneralInfoPrsr implements IParser<Patient> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
	 private int sUserId ;//
     private int sVendorId;//
     private String sLoginName ;
     private String sPassword ;
     private String sPhoto ;
     private int iSecurityLevelId ;
     private String sFirstName ;//
     private String sLastName ;//
     private String sBirthDate ;//
     private int sGender ;//
     private int sCountryId;//
     private String sProvience;
     private String sEmail;//
     private String sHomeAddress;//
     private String sPhoneAtHome;//
     private String sMobileNum;//
     private int ivendId;
     private boolean bEnDebug;
     private String generalId;        
     private String accountNumber;
     private String residentCode;

	
	public PatientGeneralInfoPrsr() {
	}


	@Override
	public Patient parse() {
		Patient patient = new Patient();

		try {	
			 patient.setUserId(sUserId);
			 patient.setFirstName(sFirstName);
			 patient.setLastName(sLastName);
			 patient.setAddress(sHomeAddress);
			 patient.setCity(sProvience);
			 patient.setState(sProvience);
			 //patient.setGender((sGender == true)?UserGender.MALE:UserGender.FEMALE);
			switch (sGender){
				case -1:				
					patient.setGender(UserGender.UNKNOWN);
					break;
				case 0:
					patient.setGender(UserGender.FEMALE);
					break;
				case 1:
					patient.setGender(UserGender.MALE);
					break;
				}
			 patient.setPhone(sMobileNum);//Incorrect is mobile not phone at home
			 patient.setMobile(sMobileNum);
			 patient.setBirthDate(Util.parseDate(sBirthDate, Util.DATEFORMAT_INPUT));
			 patient.setGeneralId(generalId);
			 patient.setAccountNumber(accountNumber);
			 patient.setResidentCode(residentCode);
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patient;
	}

	@JsonProperty("UserId")
	public int getsUserId() {
		return sUserId;
	}

	@JsonProperty("UserId")
	public void setsUserId(int sUserId) {
		this.sUserId = sUserId;
	}

	@JsonProperty("VendorId")
	public int getsVendorId() {
		return sVendorId;
	}

	@JsonProperty("VendorId")
	public void setsVendorId(int sVendorId) {
		this.sVendorId = sVendorId;
	}


	public String getsLoginName() {
		return sLoginName;
	}


	public void setsLoginName(String sLoginName) {
		this.sLoginName = sLoginName;
	}


	public String getsPassword() {
		return sPassword;
	}


	public void setsPassword(String sPassword) {
		this.sPassword = sPassword;
	}


	public String getsPhoto() {
		return sPhoto;
	}


	public void setsPhoto(String sPhoto) {
		this.sPhoto = sPhoto;
	}


	public int getiSecurityLevelId() {
		return iSecurityLevelId;
	}


	public void setiSecurityLevelId(int iSecurityLevelId) {
		this.iSecurityLevelId = iSecurityLevelId;
	}

	@JsonProperty("firstName")
	public String getsFirstName() {
		return sFirstName;
	}

	@JsonProperty("firstName")
	public void setsFirstName(String sFirstName) {
		this.sFirstName = sFirstName;
	}

	@JsonProperty("lastName")
	public String getsLastName() {
		return sLastName;
	}

	@JsonProperty("lastName")
	public void setsLastName(String sLastName) {
		this.sLastName = sLastName;
	}

	@JsonProperty("birthDate")
	public String getsBirthDate() {
		return sBirthDate;
	}

	@JsonProperty("birthDate")
	public void setsBirthDate(String sBirthDate) {
		this.sBirthDate = sBirthDate;
	}

	@JsonProperty("gender")
	public int issGender() {
		return sGender;
	}

	@JsonProperty("gender")
	public void setsGender(int sGender) {
		this.sGender = sGender;
	}

	@JsonProperty("countryId")
	public int getsCountryId() {
		return sCountryId;
	}

	@JsonProperty("countryId")
	public void setsCountryId(int sCountryId) {
		this.sCountryId = sCountryId;
	}


	public String getsProvience() {
		return sProvience;
	}


	public void setsProvience(String sProvience) {
		this.sProvience = sProvience;
	}


	@JsonProperty("Email")
	public String getsEmail() {
		return sEmail;
	}

	@JsonProperty("Email")
	public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}

	@JsonProperty("address")
	public String getsHomeAddress() {
		return sHomeAddress;
	}

	@JsonProperty("address")
	public void setsHomeAddress(String sHomeAddress) {
		this.sHomeAddress = sHomeAddress;
	}

	@JsonProperty("PhoneAtHome")
	public String getsPhoneAtHome() {
		return sPhoneAtHome;
	}

	@JsonProperty("PhoneAtHome")
	public void setsPhoneAtHome(String sPhoneAtHome) {
		this.sPhoneAtHome = sPhoneAtHome;
	}

	@JsonProperty("mobile")
	public String getsMobileNum() {
		return sMobileNum;
	}

	@JsonProperty("mobile")
	public void setsMobileNum(String sMobileNum) {
		this.sMobileNum = sMobileNum;
	}


	public int getIvendId() {
		return ivendId;
	}


	public void setIvendId(int ivendId) {
		this.ivendId = ivendId;
	}


	public boolean isbEnDebug() {
		return bEnDebug;
	}


	public void setbEnDebug(boolean bEnDebug) {
		this.bEnDebug = bEnDebug;
	}


	public String getGeneralId() {
		return generalId;
	}


	public void setGeneralId(String generalId) {
		this.generalId = generalId;
	}

	@JsonProperty("accountNumber")
	public String getAccountNumber() {
		return accountNumber;
	}

	@JsonProperty("accountNumber")
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@JsonProperty("residentCode")
	public String getResidentCode() {
		return residentCode;
	}

	@JsonProperty("residentCode")
	public void setResidentCode(String residentCode) {
		this.residentCode = residentCode;
	}


	
	
}
