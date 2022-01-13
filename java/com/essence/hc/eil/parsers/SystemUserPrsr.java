package com.essence.hc.eil.parsers;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.Installation;
import com.essence.hc.model.Language;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.PatientSettings;
import com.essence.hc.model.SystemUser;
import com.essence.hc.model.User;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.User.UserGender;
import com.essence.hc.model.User.UserType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.util.Util;
import com.essence.hc.model.VendorConfigurationTelehealth;

/**
 * 
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemUserPrsr implements IParser<SystemUser> {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	private int userId;
	private String vendorId;
	private String generalId;
	private int userType;
	private String firstName;
	private String lastName;
	private String photo;
	private String mobile;
	private String address;
	private String phone;
	private String loginName;
	private String password;
	private String birthDate;
	private int gender;
	private int countryId;
	private String province;
	private String zipCode;
	private String email;
	private PanelPrsr panelInfo;
	private AlertConfigPrsr alertConf;
	private int iLanguageId;
	private String accountNumber;
	private boolean assigned;
	private boolean mobileModeOnDesktop;
	private String servicePackageCode;
	private String careGiverType;
	private String panelTime;
	private String residentCode;
	private boolean enableEmergencyCall;
	private String emergencyNumberType;
	private String customEmergencyNumber;
	private VendorConfigurationTelehealth teleHealth;

	public SystemUserPrsr() {
	}

	@Override
	public SystemUser parse() {

		SystemUser systemUser = null;

		try {
			switch (userType) {
			case 1:
				systemUser = new User();
				systemUser.setUserType(UserType.ROLE_ADMIN);
				break;
			case 2:
				systemUser = new User();
				systemUser.setUserType(UserType.ROLE_CAREGIVER);
				systemUser.setAccountNumber(accountNumber);
				systemUser.setMobileModeOnDesktop(mobileModeOnDesktop);
				break;
			case 3:
				systemUser = new Patient();
				Patient patient = (Patient) systemUser;

				systemUser.setUserType(UserType.ROLE_MONITORED);

				if (panelInfo != null) {
					Panel panel = (Panel) panelInfo.parse();
					patient.setAccountNumber(panel.getAccount());
					patient.setInstallation(new Installation());
					patient.getInstallation().setPanel(panel);
					patient.setIsActiveService(panel.isSupportActiveService());
				}

				if (panelTime != null) {
					patient.setPanelTime(panelTime);
				}

				PatientSettings settings = new PatientSettings();
				patient.setPatientSettings(settings);
				settings.setEnableCall(enableEmergencyCall);

				if (!StringUtils.isEmpty(this.emergencyNumberType)) {
					settings.setPhoneNumberType(this.emergencyNumberType);
				} else {
					settings.setPhoneNumberType("Default");
				}

				if (!StringUtils.isEmpty(this.customEmergencyNumber)) {
					settings.setCustomPhoneNumber(this.customEmergencyNumber);
				}
				
				patient.setResidentCode(residentCode);

				break;
			}

			systemUser.setServiceType(
					ServiceType.fromString(Util.mapFromNewToOldServicePackageNaming(servicePackageCode)));
			systemUser.setUserId(userId);
			systemUser.setFirstName(firstName);
			systemUser.setLastName(lastName);
			systemUser.setAddress(address);
			systemUser.setMobile(mobile);
			systemUser.setPhone(phone);
			systemUser.setNick(loginName);
			systemUser.setEncodedPasswd(password);
			systemUser.setGeneralId(generalId);
			systemUser.setLanguage(Language.getLanguage(iLanguageId));
			systemUser.setAssigned(assigned);
			systemUser.setEmail(email);

			if (this.teleHealth != null) {
				systemUser.setTelehealth(String.valueOf(this.teleHealth.isEnabled()));
			}

			if (birthDate != null) {
				Date birth = Util.parseDate(birthDate, Util.DATEFORMAT_INPUT);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(birth);
				if (calendar.get(Calendar.YEAR) > 1) {
					systemUser.setBirthDate(birth);
				}
			}

			switch (gender) {
			case -1:
				systemUser.setGender(UserGender.UNKNOWN);
				break;
			case 0:
				systemUser.setGender(UserGender.FEMALE);
				break;
			case 1:
				systemUser.setGender(UserGender.MALE);
				break;
			}
			/*
			 * if (optString("gender",null)){
			 * systemUser.setGender((gender==true)?UserGender.MALE : UserGender.FEMALE);
			 * }else{ systemUser.setGender(UserGender.UNKNOWN); }
			 */

			if (alertConf != null) {
				Map<String, Boolean> basicAlertConf = alertConf.parse();
				AlertPreferences alertPrefs = (AlertPreferences) AlertPreferencesPrsr.getBasicPrefs(basicAlertConf);
				systemUser.setAlertPrefs(alertPrefs);
			}

			if ("MasterCareGiver".equals(careGiverType)) {
				systemUser.setCaregiverType(CaregiverType.MASTER);
			} else {
				systemUser.setCaregiverType(CaregiverType.STANDARD);
			}

		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		return systemUser;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public VendorConfigurationTelehealth getTelehealth() {
		return teleHealth;
	}

	public void setTeleHealth(VendorConfigurationTelehealth teleHealth) {
		this.teleHealth = teleHealth;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		String dayBirthdate = birthDate.split("T")[0];
		this.birthDate = dayBirthdate;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public boolean getAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PanelPrsr getPanelInfo() {
		return panelInfo;
	}

	public void setPanelInfo(PanelPrsr panelInfo) {
		this.panelInfo = panelInfo;
	}

	@JsonProperty("LanguageId")
	public int getiLanguageId() {
		return iLanguageId;
	}

	@JsonProperty("LanguageId")
	public void setiLanguageId(int iLanguageId) {
		this.iLanguageId = iLanguageId;
	}

	public String getGeneralId() {
		return generalId;
	}

	public void setGeneralId(String generalId) {
		this.generalId = generalId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public boolean getMobileModeOnDesktop() {
		return mobileModeOnDesktop;
	}

	public void setMobileModeOnDesktop(boolean mobileModeOnDesktop) {
		this.mobileModeOnDesktop = mobileModeOnDesktop;
	}

	// public AlertPreferencesPrsr getAlertPrefs() {
	// return alertPrefs;
	// }
	//
	// public void setAlertPrefs(AlertPreferencesPrsr alertPrefs) {
	// this.alertPrefs = alertPrefs;
	// }

	public AlertConfigPrsr getAlertConf() {
		return alertConf;
	}

	public void setAlertConf(AlertConfigPrsr alertConf) {
		this.alertConf = alertConf;
	}

	@JsonProperty("CareGiverType")
	public String getCareGiverType() {
		return careGiverType;
	}

	@JsonProperty("CareGiverType")
	public void setCareGiverType(String careGiverType) {
		this.careGiverType = careGiverType;
	}

	@JsonProperty("panelTime")
	public String getPanelTime() {
		return panelTime;
	}

	@JsonProperty("panelTime")
	public void setPanelTime(String panelTime) {
		this.panelTime = panelTime;
	}

	public boolean isEnableEmergencyCall() {
		return enableEmergencyCall;
	}

	public void setEnableEmergencyCall(boolean enableEmergencyCall) {
		this.enableEmergencyCall = enableEmergencyCall;
	}

	public String getEmergencyNumberType() {
		return emergencyNumberType;
	}

	public void setEmergencyNumberType(String emergencyNumberType) {
		this.emergencyNumberType = emergencyNumberType;
	}

	public String getCustomEmergencyNumber() {
		return customEmergencyNumber;
	}

	public void setCustomEmergencyNumber(String customEmergencyNumber) {
		this.customEmergencyNumber = customEmergencyNumber;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}
	
	// EIC15-2456: Attach JSON and methofs for residentCode

	 @JsonProperty("residentCode")
	public String getResidentCode() {
		return residentCode;
	}

	@JsonProperty("residentCode")
	public void setResidentCode(String residentCode) {
		this.residentCode = residentCode;
	}

}
