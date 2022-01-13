package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.EventLog;
import com.essence.hc.model.SesLogFrm;
import com.essence.hc.model.SystemStatus.StatusTypes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventLogPrsr implements IParser<EventLog> {
	
	private boolean bNewUpdates;
	private int eSysStstus;
	private int iMxLogId;
	private String sMainAlrt;
	private String sSesDt;
	private SesLogFrmPrsr[] pFrm;

	
	public EventLogPrsr() {
	}


	@Override
	public EventLog parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
			EventLog eventLog = new EventLog();	
			try {			
				eventLog.setbNewUpdates(bNewUpdates);
				eventLog.setiMxLogId(iMxLogId);
				eventLog.setsMainAlrt(sMainAlrt);
				eventLog.setsSesDt(sSesDt);
				switch (eSysStstus){
				case 0: eventLog.setSessionState(StatusTypes.NO_ALERTS);
						break;
				case 1: eventLog.setSessionState(StatusTypes.ALARM);
						break;
				case 2: eventLog.setSessionState(StatusTypes.ALARM_IN_PROGRESS);
						break;
				case 3: eventLog.setSessionState(StatusTypes.TECH_ALARM);
						break;
				case 4: eventLog.setSessionState(StatusTypes.USER_PHOTO_REQUEST);
						break;
				}

				if (pFrm!=null){
					SesLogFrm[] logResponse = new SesLogFrm[pFrm.length];
					for (int i = 0; i <= pFrm.length - 1; i++){
						logResponse[i] = pFrm[i].parse();
						eventLog.setpFrm(logResponse);
					}
				}
	
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return eventLog;
	}


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


	public SesLogFrmPrsr[] getpFrm() {
		return pFrm;
	}


	public void setpFrm(SesLogFrmPrsr[] pFrm) {
		this.pFrm = pFrm;
	}


	public int geteSysStstus() {
		return eSysStstus;
	}


	public void seteSysStstus(int eSysStstus) {
		this.eSysStstus = eSysStstus;
	}

}
