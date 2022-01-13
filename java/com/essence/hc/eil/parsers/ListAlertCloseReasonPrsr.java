package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.AlertCloseReason;

public class ListAlertCloseReasonPrsr implements IParser<List<AlertCloseReason>> {
	
	List<AlertCloseReasonPrsr> reasonList;
	
	public ListAlertCloseReasonPrsr() {
	}


	@Override
	public List<AlertCloseReason> parse() {

		List<AlertCloseReason> reasons = new ArrayList <AlertCloseReason>();
		
		try { 
			if(reasonList != null)
				for(AlertCloseReasonPrsr i: reasonList){
					reasons.add((AlertCloseReason) i.parse());
				}
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}

		return  reasons;
	}

	@JsonProperty("AlertCloseReasons")
	public List<AlertCloseReasonPrsr> getReasonList() {
		return reasonList;
	}

	@JsonProperty("AlertCloseReasons")
	public void setReasonList(List<AlertCloseReasonPrsr> reasonList) {
		this.reasonList = reasonList;
	}
	
}