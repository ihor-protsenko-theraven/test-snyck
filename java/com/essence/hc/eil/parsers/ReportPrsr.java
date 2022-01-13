package com.essence.hc.eil.parsers;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Patient;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportPrsr implements IParser<Patient> {


//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
	private PatientGeneralInfoPrsr user;
	private Date sReportDateInit;
	private Date sReportDateEnd;
     
	
	public ReportPrsr() {
	}


	@Override
	public Patient parse() {
		Patient patient = new Patient();
		if (user!=null)
			patient = user.parse();

		try {			
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return patient;
	}

	
	public PatientGeneralInfoPrsr getuser() {
		return user;
	}


	public void setuser(PatientGeneralInfoPrsr user) {
		this.user = user;
	}

	@JsonProperty("ReportDateInit")
	public Date getsReportDateInit() {
		return sReportDateInit;
	}

	@JsonProperty("ReportDateInit")
	public void setsReportDateInit(Date sReportDateInit) {
		this.sReportDateInit = sReportDateInit;
	}

	@JsonProperty("ReportDateEnd")
	public Date getsReportDateEnd() {
		return sReportDateEnd;
	}

	@JsonProperty("ReportDateEnd")
	public void setsReportDateEnd(Date sReportDateEnd) {
		this.sReportDateEnd = sReportDateEnd;
	}



	
}
