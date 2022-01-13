package com.essence.hc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * A logged-in user
 * 
 * Usually a Caregiver or an Admin, but could be any of the user roles defined.
 */
public class User extends SystemUser implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Set after the user successfully logged in
	 */
	private PasswordExpirationData passwordExpirationData;
	private Eula eula;
	
	public static enum UserType {ROLE_ADMIN(1), ROLE_CAREGIVER(2), ROLE_MONITORED(3);
		private final int id;
		UserType(int id) { this.id = id;}
		public int getId() {return id;}
	}
	
	public static enum UserGender {UNKNOWN(-1), FEMALE(0), MALE(1);
		private final int id;
		UserGender(int id) { this.id = id;}
		public int getId() {return id;}
	}
	
	// UserGender for new External GetMonitoring API (2.5.10)
	public static enum Gender {UNKNOWN("Unknown"), FEMALE("Female"), MALE("Male");
		private final String name;
		Gender(String name) { this.name = name;}
		public String getName() {return name;}
		public String toString() {return name;}
		
		public static Gender getGender(String text) {
			if (StringUtils.hasText(text)) {
				for (Gender gender : Gender.values()) {
					if (text.equalsIgnoreCase(gender.name)) {
						return gender;
					}
				}
			}
			return UNKNOWN;
		}
	}
	
	public static enum CaregiverType {STANDARD, MASTER};
	
	private boolean enabled;
	private List<GrantedAuthority> AUTHORITIES;
	private long expireSession = System.currentTimeMillis();
	private boolean sessionExpired = false;
	private boolean sessionExpirationDisabled = false;
	private boolean sessionStartedByExternalSystem = false;
	private boolean isTokenExpired;
	
	// patients list
	private List<Patient> patients = new ArrayList<Patient>();
	// alerts list
	private List<Alert> currentAlerts = new ArrayList<Alert>();
	
	private Patient currentPatient;
	
	private List<Patient> currentPatients;

	private List<ExternalAPIResidentMonitoring> residentsMonitoring;

	//default constructor can be deleted in the future...
	public User(){};
	
	/**
	 * Constructor for authentication 
	 * @param user
	 * @param pwd
	 * @param authList
	 */
	public User(String user, String pwd, List<GrantedAuthority> authList){
		this.nick = user;
		this.passwd = pwd;
		this.AUTHORITIES = authList;
		this.enabled = true;
	}

	/**
	 * Hack for some legacy methods (before Patient becomes subclass of SytemUser) 
	 * which require patient's id as String
	 */
	public String getId() {
		return String.valueOf(userId);
	}
	
	public List<Patient> getPatients() {
		return patients;
	}
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
	public List<Alert> getCurrentAlerts() {
		return currentAlerts;
	}
	public void setCurrentAlerts(List<Alert> currentAlerts) {
		this.currentAlerts = currentAlerts;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.AUTHORITIES;
	}
	public void setAUTHORITIES(List<GrantedAuthority> aUTHORITIES) {
		AUTHORITIES = aUTHORITIES;
	}
	public Patient getCurrentPatient() {
		return currentPatient;
	}
	public void setCurrentPatient(Patient currentPatient) {
		this.currentPatient = currentPatient;
	}

	public List<Patient> getCurrentPatients() {
		return currentPatients;
	}

	public void setCurrentPatients(List<Patient> currentPatients) {
		this.currentPatients = currentPatients;
	}

	public List<ExternalAPIResidentMonitoring> getResidentsMonitoring() {
		return residentsMonitoring;
	}

	public void setResidentsMonitoring(List<ExternalAPIResidentMonitoring> residentsMonitoring) {
		this.residentsMonitoring = residentsMonitoring;
	}

	// TODO: check if any of these attrs are necessary
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return passwd;
	}

	@Override
	public String getUsername() {
		return nick;
	}

	public long getExpireSession() {
		return expireSession;
	}

	public void setExpireSession(long expireSession) {
		this.expireSession = expireSession;
	}

	public boolean isSessionExpired() {
		return sessionExpired;
	}

	public void setSessionExpired(boolean sessionExpired) {
		this.sessionExpired = sessionExpired;
	}

	public boolean isSessionStartedByExternalSystem() {
		return sessionStartedByExternalSystem;
	}

	public void setSessionStartedByExternalSystem(boolean sessionStartedByExternalSystem) {
		this.sessionStartedByExternalSystem = sessionStartedByExternalSystem;
	}

	public PasswordExpirationData getPasswordExpirationData() {
		return passwordExpirationData;
	}

	public void setPasswordExpirationData(PasswordExpirationData passwordExpirationData) {
		this.passwordExpirationData = passwordExpirationData;
	}

	public boolean isPasswordAboutToExpire() {
		
		if(this.passwordExpirationData == null) {
			return false;
		}
		return this.passwordExpirationData.getAboutToExpireDays() >= this.passwordExpirationData.getPasswordExpirationDays();
	}

	public Eula getEula() {
		return eula;
	}

	public void setEula(Eula eula) {
		this.eula = eula;
	}

	public boolean isSessionExpirationDisabled() {
		return sessionExpirationDisabled;
	}

	public void setSessionExpirationDisabled(boolean sessionExpirationDisabled) {
		this.sessionExpirationDisabled = sessionExpirationDisabled;
	}

	public boolean isTokenExpired() {
		return isTokenExpired;
	}

	public void setTokenExpired(boolean isTokenExpired) {
		this.isTokenExpired = isTokenExpired;
	}
}
