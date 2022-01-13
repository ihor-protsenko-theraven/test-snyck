package com.essence.hc.eil.parsers;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;
import com.essence.hc.model.Alert.AlertState;


public class AlertCountPrsr implements IParser<Patient> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
	private int iPatientId;
	private String iCpId;
	private int iAlrtNum;
	private int iLastAlrtId;
	private int iMinAlrtSts;
     
	
	public AlertCountPrsr() {
	}


	@Override
	public Patient parse() {
		Patient patient = new Patient();
		try {			
			patient.setUserId(iPatientId);
			patient.setNumAlerts(iAlrtNum);
			patient.setiLastAlrtId(iLastAlrtId);
			patient.setCpId(iCpId);
			patient.setMinAlertState(AlertState.parseInt(iMinAlrtSts));
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patient;
	}


	public int getiPatientId() {
		return iPatientId;
	}


	public void setiPatientId(int iPatientId) {
		this.iPatientId = iPatientId;
	}


	public String getiCpId() {
		return iCpId;
	}


	public void setiCpId(String iCpId) {
		this.iCpId = iCpId;
	}


	public int getiAlrtNum() {
		return iAlrtNum;
	}


	public void setiAlrtNum(int iAlrtNum) {
		this.iAlrtNum = iAlrtNum;
	}


	public int getiLastAlrtId() {
		return iLastAlrtId;
	}


	public void setiLastAlrtId(int iLastAlrtId) {
		this.iLastAlrtId = iLastAlrtId;
	}


	public int getiMinAlrtSts() {
		return iMinAlrtSts;
	}


	public void setiMinAlrtSts(int iMinAlrtSts) {
		this.iMinAlrtSts = iMinAlrtSts;
	}

	
	

}
