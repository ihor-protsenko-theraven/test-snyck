package com.essence.hc.eil.parsers;

import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ExternalAPISystemUserPrsr implements IParser<SystemUser>, IGenerator<SystemUser> {

	private Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());

	private String userType;
	private String userName;
	private String firstName;
	private String lastName;
	private VendorConfigurationTelehealth teleHealth;
	private String identificationNumber;
	private String birthDate;
	private String gender;
	private String email;
	private String cellPhoneNumber;
	private int languageId;
	private String address;
	private String homePhone;
	private String caregiverType;
	private String password;
	private int userId;
	private boolean mobileViewOnDesktop;
	private String panelServicePackages;
	private String servicePackageCode;
	private ActiveEmergencyCallUserSettingsPrsr settings;

	@Override
	public SystemUser parse() {
		SystemUser systemUser = null;

		if ("Administrator".equals(userType)) {
			systemUser = new User();
			systemUser.setUserType(UserType.ROLE_ADMIN);
		} else if ("CareGiver".equals(userType)) {
			systemUser = new User();
			systemUser.setUserType(UserType.ROLE_CAREGIVER);
			if ("MasterCareGiver".equals(caregiverType)) {
				systemUser.setCaregiverType(CaregiverType.MASTER);
			} else {
				systemUser.setCaregiverType(CaregiverType.STANDARD);
			}
			systemUser.setMobileModeOnDesktop(mobileViewOnDesktop);
		} else if ("Resident".equals(userType)) {
			Patient patient = new Patient();
			patient.setUserType(UserType.ROLE_MONITORED);
			if (settings != null && settings.getActiveEmergencyCall() != null) {
				PatientSettings patientSettings = new PatientSettings();
				patient.setPatientSettings(patientSettings);
				patientSettings.setEnableCall(settings.getActiveEmergencyCall().isEnableCall());

				if (StringUtils.hasText(settings.getActiveEmergencyCall().getPhoneNumberType())) {
					patientSettings.setPhoneNumberType(this.settings.getActiveEmergencyCall().getPhoneNumberType());
				} else {
					patientSettings.setPhoneNumberType("Default");
				}

				if (StringUtils.hasText(settings.getActiveEmergencyCall().getCustomPhoneNumber())) {
					patientSettings.setCustomPhoneNumber(settings.getActiveEmergencyCall().getCustomPhoneNumber());
				}

			}

			if (StringUtils.hasText(this.panelServicePackages)) {
				patient.setPanelServicePackages(this.panelServicePackages);
			}
			if (StringUtils.hasText(this.servicePackageCode)) {
				patient.setServicePackageCode(this.servicePackageCode);
				patient.setServiceType(
						ServiceType.fromString(Util.mapFromNewToOldServicePackageNaming(this.servicePackageCode)));
			}
			systemUser = patient;
		} else {
			return null;
		}

		systemUser.setUserId(userId);
		systemUser.setNick(userName);
		systemUser.setFirstName(firstName);
		systemUser.setLastName(lastName);
		systemUser.setGeneralId(identificationNumber);
		systemUser.setEmail(email);
		systemUser.setMobile(cellPhoneNumber);
		systemUser.setLanguageId(languageId);
		systemUser.setPhone(homePhone);
		if (birthDate != null) {
			Date birth = null;
			try {
				birth = Util.parseDate(birthDate, Util.DATEFORMAT_INPUT);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(birth);
				if (calendar.get(Calendar.YEAR) > 1) {
					systemUser.setBirthDate(birth);
				}
			} catch (ParseException e) {
			}
		}
		if ("Male".equals(gender)) {
			systemUser.setGender(UserGender.MALE);
		} else if ("Female".equals(gender)) {
			systemUser.setGender(UserGender.FEMALE);
		} else {
			systemUser.setGender(UserGender.UNKNOWN);
		}

		return systemUser;
	}

	@Override
	public void load(SystemUser user) {
		this.setUserId(user.getUserId());
		this.setUserName(user.getNick());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		// this.setIdentificationNumber(Integer.toString(user.getUserId()));
		this.setIdentificationNumber(user.getGeneralId());
		
		this.setTelehealth(user.getTelehealth());
		
		if (user.getBirthDate() != null)
			this.setBirthDate(Util.formatDate(user.getBirthDate(), Util.DATEFORMAT_EXTERNAL_API));
		if (user.getGender() == UserGender.FEMALE) {
			this.setGender("Female");
		} else if (user.getGender() == UserGender.MALE) {
			this.setGender("Male");
		} else {
			this.setGender("");
		}
		this.setEmail(user.getEmail());
		this.setCellPhoneNumber(user.getMobile());
		this.setLanguageId(user.getLanguage().getLanguageID());

		this.setAddress(user.getAddress());
		this.setHomePhone(user.getPhone());

		switch (user.getUserType()) {
		case ROLE_ADMIN:
			this.setUserType("Administrator");
			break;
		case ROLE_CAREGIVER:
			this.setUserType("CareGiver");
			if (user.getCaregiverType() == CaregiverType.MASTER) {
				this.setCaregiverType("MasterCareGiver");
			} else {
				this.setCaregiverType("StandardCareGiver");
			}
			this.setMobileViewOnDesktop(user.getMobileModeOnDesktop());
			break;
		case ROLE_MONITORED:
			this.setUserType("Resident");
			if (user.getNick() == null) {
				this.setUserName("");
			}

			ActiveEmergencyCallUserPrsr activeEmergencyCall = new ActiveEmergencyCallUserPrsr();
			activeEmergencyCall.setEnableCall(((Patient) user).getPatientSettings().isEnableCall());
			activeEmergencyCall.setPhoneNumberType(((Patient) user).getPatientSettings().getPhoneNumberType());
			activeEmergencyCall.setCustomPhoneNumber(((Patient) user).getPatientSettings().getCustomPhoneNumber());

			this.settings = new ActiveEmergencyCallUserSettingsPrsr();
			this.settings.setActiveEmergencyCall(activeEmergencyCall);

			break;
		}

		if (user.getUserType() != UserType.ROLE_MONITORED) {

			this.setPassword(user.getPasswd());
		}
	}

	@JsonProperty("userType")
	public String getUserType() {
		return userType;
	}

	@JsonProperty("userType")
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@JsonProperty("teleHealth")
	public VendorConfigurationTelehealth getTeleHealth() {
		return teleHealth;
	}
	
	@JsonProperty("teleHealth")
	public void setTelehealth(VendorConfigurationTelehealth teleHealth) {
		this.teleHealth = teleHealth;
	}

	@JsonProperty("userName")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("identificationNumber")
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	@JsonProperty("identificationNumber")
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	@JsonProperty("birthDate")
	public String getBirthDate() {
		return birthDate;
	}

	@JsonProperty("birthDate")
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@JsonProperty("gender")
	public String getGender() {
		return gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		this.gender = gender;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("cellPhoneNumber")
	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	@JsonProperty("cellPhoneNumber")
	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	@JsonProperty("languageId")
	public int getLanguageId() {
		return languageId;
	}

	@JsonProperty("languageId")
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty("homePhone")
	public String getHomePhone() {
		return homePhone;
	}

	@JsonProperty("homePhone")
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@JsonProperty("careGiverType")
	public String getCaregiverType() {
		return caregiverType;
	}

	@JsonProperty("careGiverType")
	public void setCaregiverType(String caregiverType) {
		this.caregiverType = caregiverType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isMobileViewOnDesktop() {
		return mobileViewOnDesktop;
	}

	public void setMobileViewOnDesktop(boolean mobileViewOnDesktop) {
		this.mobileViewOnDesktop = mobileViewOnDesktop;
	}

	public ActiveEmergencyCallUserSettingsPrsr getSettings() {
		return settings;
	}

	public void setSettings(ActiveEmergencyCallUserSettingsPrsr settings) {
		this.settings = settings;
	}

	public String getPanelServicePackages() {
		return panelServicePackages;
	}

	public void setPanelServicePackages(String panelServicePackages) {
		this.panelServicePackages = panelServicePackages;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}
}
