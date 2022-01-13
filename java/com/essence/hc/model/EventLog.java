package com.essence.hc.model;

import java.io.Serializable;

import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.SystemStatus.StatusTypes;

/**
*	
* 
* @author daniel.alcantarilla
*
*/
public class EventLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean bNewUpdates;
	private int iMxLogId;
	private StatusTypes sessionState;
	private String sMainAlrt;
	private String sSesDt;
	private SesLogFrm[] pFrm;
	
	public boolean isbNewUpdates() {
		return bNewUpdates;
	}
	public void setbNewUpdates(boolean bNewUpdates) {
		this.bNewUpdates = bNewUpdates;
	}
	public int getiMxLogId() {
		return iMxLogId;
	}
	public void setiMxLogId(int iMxLogId) {
		this.iMxLogId = iMxLogId;
	}
	public String getsMainAlrt() {
		return sMainAlrt;
	}
	public void setsMainAlrt(String sMainAlrt) {
		this.sMainAlrt = sMainAlrt;
	}
	public String getsSesDt() {
		return sSesDt;
	}
	public void setsSesDt(String sSesDt) {
		this.sSesDt = sSesDt;
	}
	public SesLogFrm[] getpFrm() {
		return pFrm;
	}
	public void setpFrm(SesLogFrm[] pFrm) {
		this.pFrm = pFrm;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public StatusTypes getSessionState() {
		return sessionState;
	}
	public void setSessionState(StatusTypes sessionState) {
		this.sessionState = sessionState;
	}
	
	
	
}
