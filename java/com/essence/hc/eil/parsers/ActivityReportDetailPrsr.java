package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;
import com.essence.hc.util.Util;
import com.essence.hc.eil.exceptions.ParseException;
/* NEW */
import com.essence.hc.model.ActivityReportDetail;

/**
 * @author gerardo.rodriguez
 *
 */
public class ActivityReportDetailPrsr implements IParser<ActivityReportDetail> {
	/* NEW */
	private String date;
	private String firstActivity;
	private String lastActivity;
	private int restroom;
	private int bathroom;
	private int diningroom;
	private int bedroom;
	private int livingroom;
	private int otherroom;
	private boolean outOfHome;
	
	@Override
	public ActivityReportDetail parse() {
		ActivityReportDetail activityReportDetail = new ActivityReportDetail();
		
		//SETERS
		try{
			activityReportDetail.setDate(Util.parseDate(date, Util.DATEFORMAT_INPUT));
			/*if("".equals(firstActivity)){
				firstActivity = null;
			}else{
				activityReportDetail.setFirstActivity(Util.parseDateTime(firstActivity, Util.TIMEFORMAT_SHORT));
			}
			if("".equals(lastActivity)){ 
				lastActivity = null;
			}else{
				activityReportDetail.setLastActivity(Util.parseDateTime(lastActivity, Util.TIMEFORMAT_SHORT));
			}*/
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}		
		
		activityReportDetail.setFirstActivity(firstActivity);
		activityReportDetail.setLastActivity(lastActivity);
		
		activityReportDetail.setRestroom(restroom);
		activityReportDetail.setBathroom(bathroom);
		activityReportDetail.setDiningroom(diningroom);
		activityReportDetail.setBedroom(bedroom);
		activityReportDetail.setLivingroom(livingroom);
		activityReportDetail.setOtherroom(otherroom);
		activityReportDetail.setOutOfHome(outOfHome);
		
		return activityReportDetail;
	}
	
	
	
	@JsonProperty("Date")
	public String getDate(){
		return date;
	}
	@JsonProperty("Date")
	public void setDate(String date){
		this.date = date;
	}
	
	
	@JsonProperty("FirstActivity")
	public String getFirstActivity(){
		return firstActivity;
	}
	@JsonProperty("FirstActivity")
	public void setFirstActivity(String firstActivity){
		this.firstActivity = firstActivity;
	}
	
	
	@JsonProperty("LastActivity")
	public String getLastActivity(){
		return lastActivity;
	}
	@JsonProperty("LastActivity")
	public void setLastActivity(String lastActivity){
		this.lastActivity = lastActivity;
	}
	
	
	@JsonProperty("Restroom")
	public int getRestroom(){
		return restroom;
	}
	@JsonProperty("Restroom")
	public void setRestroom(int restroom){
		this.restroom = restroom;
	}
	
	
	@JsonProperty("Bathroom")
	public int getBathroom(){
		return bathroom;
	}
	@JsonProperty("Bathroom")
	public void setBathroom(int bathroom){
		this.bathroom = bathroom;
	}
	
	
	@JsonProperty("DiningRoom")
	public int getDiningroom(){
		return diningroom;
	}
	@JsonProperty("DiningRoom")
	public void setDiningRoom(int diningroom){
		this.diningroom = diningroom;
	}
	
	
	@JsonProperty("Bedroom")
	public int getBedroom(){
		return bedroom;
	}
	@JsonProperty("Bedroom")
	public void setBedroom(int bedroom){
		this.bedroom = bedroom;
	}
	
	
	@JsonProperty("LivingRoom")
	public int getLivingRoom(){
		return livingroom;
	}
	@JsonProperty("LivingRoom")
	public void setLivingRoom(int livingroom){
		this.livingroom = livingroom;
	}
	
	
	@JsonProperty("OtherRoom")
	public int getOtherRoom(){
		return otherroom;
	}
	@JsonProperty("OtherRoom")
	public void setOtherRoom(int otherroom){
		this.otherroom = otherroom;
	}
	
	
	@JsonProperty("OutOfHome")
	public boolean getOutOfHome(){
		return outOfHome;
	}
	@JsonProperty("OutOfHome")
	public void setOutOfHome(boolean outOfHome){
		this.outOfHome = outOfHome;
	}
}
