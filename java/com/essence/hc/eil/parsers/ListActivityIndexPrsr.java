package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ActivityIndex;

/**
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListActivityIndexPrsr implements IParser<List<ActivityIndex>> {


//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<ActivityIndexPrsr> activityIndexData;
	

	@Override
	public List<ActivityIndex> parse() {

		List<ActivityIndex> activityIndexList = new ArrayList<>();
		
		try { 
			if(activityIndexData != null)		 
				for(ActivityIndexPrsr activityIndexPrsr: activityIndexData){
					activityIndexList.add((ActivityIndex) activityIndexPrsr.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return  activityIndexList;
	}

    @JsonProperty("ActivityIndexData")
	public List<ActivityIndexPrsr> getActivityIndexData() {
		return activityIndexData;
	}

    @JsonProperty("ActivityIndexData")
	public void setActivityIndexData(List<ActivityIndexPrsr> activityIndexData) {
		this.activityIndexData = activityIndexData;
	}



	
}
