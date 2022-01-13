package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;

/**
 * 
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitoringPrsr implements IParser<List<Patient>> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	ArrayList<MonitoringElementPrsr> patientInfoList;
	
	public MonitoringPrsr() {
	}


	@Override
	public List<Patient> parse() {
		List<Patient> patients = new ArrayList <Patient>();
			
		try {	
			
			if(patientInfoList !=null )
				for(MonitoringElementPrsr i: patientInfoList){
					patients.add((Patient)i.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patients;
	}


	public ArrayList<MonitoringElementPrsr> getPatientInfoList() {
		return patientInfoList;
	}


	public void setPatientInfoList(ArrayList<MonitoringElementPrsr> patientInfoList) {
		this.patientInfoList = patientInfoList;
	}


	
	
}
