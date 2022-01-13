package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIStepCountReportPrsr implements IParser<StepCountReport> {

	ExternalAPIStepCountReportDataPrsr stepCountReport;
	
	@Override
	public StepCountReport parse() {
		
		StepCountReport stepCountReport = new StepCountReport();
		
		if(this.stepCountReport != null) {
			stepCountReport = this.stepCountReport.parse();
		}
		
		return stepCountReport;
	}

	public ExternalAPIStepCountReportDataPrsr getStepCountReport() {
		return stepCountReport;
	}

	public void setStepCountReport(ExternalAPIStepCountReportDataPrsr stepCountReport) {
		this.stepCountReport = stepCountReport;
	}

}
