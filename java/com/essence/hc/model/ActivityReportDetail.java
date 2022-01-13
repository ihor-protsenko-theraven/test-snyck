package com.essence.hc.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * @author gerardo.rodriguez
 * @description: this class is for manage data of ADL 2.2 Index Activity Report.
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityReportDetail {
	
/* PROPERTIES */
	private Date Date;
	private String FirstActivity;
	private String LastActivity;
	private int Restroom;
	private int Bathroom;
	private int Diningroom;
	private int Bedroom;
	private int Livingroom;
	private int Otherroom;
	private boolean OutOfHome;

	
/* METHODS */
	public Date getDate(){
		return Date;
	}
	
	public void setDate(Date Date){
		this.Date = Date;
	}
	
	
	public String getFirstActivity(){
		return FirstActivity;
	}
	
	public void setFirstActivity(String FirstActivity){
		this.FirstActivity = FirstActivity;
	}
	
	
	public String getLastActivity(){
		return LastActivity;
	}
	
	public void setLastActivity(String LastActivity){
		this.LastActivity = LastActivity;
	}
	
	
	public int getRestroom(){
		return Restroom;
	}
	
	public void setRestroom(int Restroom){
		this.Restroom = Restroom;
	}
	
	
	public int getBathroom(){
		return Bathroom;
	}
	
	public void setBathroom(int Bathroom){
		this.Bathroom = Bathroom;
	}
	
	
	public int getDiningroom(){
		return Diningroom;
	}
	
	public void setDiningroom(int Diningroom){
		this.Diningroom = Diningroom;
	}
	
	
	public int getBedroom(){
		return Bedroom;
	}
	
	public void setBedroom(int Bedroom){
		this.Bedroom = Bedroom;
	}
	
	
	public int getLivingroom(){
		return Livingroom;
	}
	
	public void setLivingroom(int Livingroom){
		this.Livingroom = Livingroom;
	}
	
	public int getOtherroom(){
		return Otherroom;
	}
	
	public void setOtherroom(int Otherroom){
		this.Otherroom = Otherroom;
	}
	
	public boolean getOutOfHome(){
		return OutOfHome;
	}
	
	public void setOutOfHome(boolean OutOfHome){
		this.OutOfHome = OutOfHome;
	}
}
