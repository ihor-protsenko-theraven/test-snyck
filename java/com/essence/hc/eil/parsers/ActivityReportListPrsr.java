package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ActivityReportDetail;

/**
 * @author gerardo.rodriguez
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityReportListPrsr implements IParser<List<ActivityReportDetail>> {
	
	private List<ActivityReportDetailPrsr> ActivityIndexData;
	

	@Override
	public List<ActivityReportDetail> parse() {

		List<ActivityReportDetail> ActivityReportDetailList = new ArrayList<>();
		
		try { 
			if(ActivityIndexData != null)		 
				for(ActivityReportDetailPrsr ActivityReportDetailPrsr: ActivityIndexData){
					ActivityReportDetailList.add((ActivityReportDetail) ActivityReportDetailPrsr.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return  ActivityReportDetailList;
	}

    @JsonProperty("ActivityIndexData")
	public List<ActivityReportDetailPrsr> getActivityIndexData() {
		return ActivityIndexData;
	}

    @JsonProperty("ActivityIndexData")
	public void setActivityIndexData(List<ActivityReportDetailPrsr> ActivityIndexData) {
		this.ActivityIndexData = ActivityIndexData;
	}



	
}