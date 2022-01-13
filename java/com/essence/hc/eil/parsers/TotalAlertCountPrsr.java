package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;


public class TotalAlertCountPrsr implements IParser<List<Patient>> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
     public ArrayList<AlertCountPrsr> alertCountList;
                 
     
	

	
	public TotalAlertCountPrsr() {
	}


	@Override
	public List<Patient> parse() {
		List<Patient> patients = new ArrayList <Patient>();
		try {			
			if(alertCountList!=null)		 
				for(AlertCountPrsr i: alertCountList){
					patients.add((Patient) i.parse());
				}	
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patients;
	}


	public ArrayList<AlertCountPrsr> getAlertCountList() {
		return alertCountList;
	}


	public void setAlertCountList(ArrayList<AlertCountPrsr> alertCountList) {
		this.alertCountList = alertCountList;
	}

	
	
}
