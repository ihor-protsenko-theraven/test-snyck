package com.essence.hc.eil.parsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Alert;
import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.SystemStatus;
import com.essence.hc.model.SystemStatus.StatusTypes;


public class SystemStatusPrsr implements IParser<SystemStatus> {

	private Logger logger = (Logger) LogManager.getLogger(SystemStatusPrsr.class);
	
	private int iCpId;
	private int eSysStstus;
	private int iActvSession;
	private int iSysState;
	private String mainAlrt;
	private String sSvrt;
	private int iAlrtNum;
	private AlertPrsr alert;
	
	
	public SystemStatusPrsr() {
	}


	@Override
	public SystemStatus parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		 SystemStatus status = new SystemStatus();
		 Alert a = new Alert();
		try {			
				status.setAlertSessionId(iActvSession);
				status.setNumAlert(iAlrtNum);
				status.setSeverity(sSvrt);
				
				//Alert State 
			 	AlertState aStates[] = AlertState.values();
				int i = 0;
				while(aStates[i].getId() != iSysState && i < aStates.length) {
					i++;
				}
				status.setSysState(aStates[i]);
				
				//Alert Type
				StatusTypes aTypes[] = StatusTypes.values();
				i = 0;
				while(aTypes[i].getStatus() != eSysStstus && i < aTypes.length) {
					i++;
				}
				status.setStatusType(aTypes[i]);
				 
				//Alert Detail
				if (alert!=null){
					 a = alert.parse();			 
					 status.setAlert(a);
				}
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
		
		return status;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public int geteSysStstus() {
		return eSysStstus;
	}


	public void seteSysStstus(int eSysStstus) {
		this.eSysStstus = eSysStstus;
	}


	public int getiActvSession() {
		return iActvSession;
	}


	public void setiActvSession(int iActvSession) {
		this.iActvSession = iActvSession;
	}


	public int getiSysState() {
		return iSysState;
	}


	public void setiSysState(int iSysState) {
		this.iSysState = iSysState;
	}


	public String getMainAlrt() {
		return mainAlrt;
	}


	public void setMainAlrt(String mainAlrt) {
		this.mainAlrt = mainAlrt;
	}


	public String getsSvrt() {
		return sSvrt;
	}


	public void setsSvrt(String sSvrt) {
		this.sSvrt = sSvrt;
	}


	public int getiAlrtNum() {
		return iAlrtNum;
	}


	public void setiAlrtNum(int iAlrtNum) {
		this.iAlrtNum = iAlrtNum;
	}


	public AlertPrsr getAlert() {
		return alert;
	}


	public void setAlert(AlertPrsr alert) {
		this.alert = alert;
	}


	public int getiCpId() {
		return iCpId;
	}


	public void setiCpId(int iCpId) {
		this.iCpId = iCpId;
	}



}
