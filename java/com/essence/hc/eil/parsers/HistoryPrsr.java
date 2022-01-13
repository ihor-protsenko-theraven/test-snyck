package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Event;



public class HistoryPrsr implements IParser<List<Event>> {
	

//	SystemStatusPrsr sysStatus;
	ArrayList<AlertPrsr> alertList;
	
	public HistoryPrsr() {
	}
	

	@Override
	public List<Event> parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		List<Event> events = new ArrayList <Event>();
		
		try { 
			if(alertList!=null)		 
				for(AlertPrsr i: alertList){
					events.add((Event) i.parse());
				}

		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}

		return events;
	}


	

//	public SystemStatusPrsr getSysStatus() {
//		return sysStatus;
//	}
//
//
//	public void setSysStatus(SystemStatusPrsr sysStatus) {
//		this.sysStatus = sysStatus;
//	}


	public ArrayList<AlertPrsr> getAlertList() {
		return alertList;
	}


	public void setAlertList(ArrayList<AlertPrsr> alertList) {
		this.alertList = alertList;
	}
	
		



		
}
