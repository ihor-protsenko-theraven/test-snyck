package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientBasicInfoPrsr implements IParser<Patient> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
    private int userId;
    private int vendorId;
    private String userType;
    private String firstName;
    private String lastName;
    private String photo;
    private String mobile;
    private String address;
    private String phone;
    private String generalId;
    private String residentCode;
                  

	public PatientBasicInfoPrsr() {
	}


	@Override
	public Patient parse() {
		Patient patient = new Patient();

		try {	
			 patient.setUserId(userId);
			 patient.setFirstName(firstName);
			 patient.setLastName(lastName);
			 patient.setAddress(address);
			 patient.setPhone(phone);
			 patient.setMobile(mobile);
			 patient.setGeneralId(generalId);
			 patient.setResidentCode(residentCode);
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patient;
	}


	


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
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


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public int getVendorId() {
		return vendorId;
	}


	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}


	public String getGeneralId() {
		return generalId;
	}


	public void setGeneralId(String generalId) {
		this.generalId = generalId;
	}

	
	
}
