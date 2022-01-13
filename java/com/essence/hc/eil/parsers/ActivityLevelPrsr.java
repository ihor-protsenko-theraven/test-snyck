package com.essence.hc.eil.parsers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.essence.hc.controllers.RulesController;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ActivityLevel;
import com.essence.hc.model.ActivityLevel.ActivityDetection;
import com.essence.hc.model.StepCountAvailableReport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityLevelPrsr implements IParser<ActivityLevel> {

	private Logger logger = (Logger) LogManager.getLogger(ActivityLevelPrsr.class);
	
	
	private boolean bIsAtHome;
	private String sLastLocation;
	private String dLocationTime;
	private float fMvmetLvl;
	private int iMvmetLvlDining;
	private int iMvmetLvlBedroom;
	private int iMvmetLvlToilet;
	private int iMvmetLvlBathroom;
	private int iMvmetLvlLivingRoom;
	private int iMvmetLvlOtherRoom;
	private String sLocationLbl;
	private int activityLevelExpress; // The calculated activity level for Express Residents
	
	private List<StepCountAvailableReportPrsr> stepCountReports;
	
	public ActivityLevelPrsr() {
	}


	@Override
	public ActivityLevel parse() {
		ActivityLevel activityLevel = null;
		try {			
			activityLevel = new ActivityLevel();
			
			ActivityDetection lastLocation = null;
			lastLocation = new ActivityDetection();
			lastLocation.setLocationType(sLastLocation);
			lastLocation.setAlias(sLastLocation);
			lastLocation.setDescription(sLocationLbl);
			lastLocation.setRegisteredLevel(fMvmetLvl);
						
			//Format Date String to Date
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = formatter.parse(dLocationTime.substring(0,10)+" "+dLocationTime.substring(11,16));
			lastLocation.setDateTimeStart(date);
			lastLocation.setDateTimeEnd(date);

			activityLevel.setPatientAtHome(bIsAtHome);
			activityLevel.setLastLocation(lastLocation);

			
			ArrayList<ActivityDetection> activityDetections = new ArrayList <ActivityDetection>();
			ActivityDetection movementLvl = null;
						
			
			movementLvl = new ActivityDetection();
			movementLvl.setAlias("living_room");
			movementLvl.setRegisteredLevel(iMvmetLvlLivingRoom);
			activityDetections.add(movementLvl);
		
			movementLvl = new ActivityDetection();
			movementLvl.setAlias("bedroom_sensor");
			movementLvl.setRegisteredLevel(iMvmetLvlBedroom);
			activityDetections.add(movementLvl);
			
			movementLvl = new ActivityDetection();
			movementLvl.setAlias("bathroom_sensor");
			movementLvl.setRegisteredLevel(iMvmetLvlBathroom);
			activityDetections.add(movementLvl);
			
			movementLvl = new ActivityDetection();
			movementLvl.setAlias("meal");
			movementLvl.setRegisteredLevel(iMvmetLvlDining);
			activityDetections.add(movementLvl);
			
			movementLvl = new ActivityDetection();
			movementLvl.setAlias("toilet_room_sensor");
			movementLvl.setRegisteredLevel(iMvmetLvlToilet);
			activityDetections.add(movementLvl);

			movementLvl = new ActivityDetection();
			movementLvl.setAlias("other_room");
			movementLvl.setRegisteredLevel(iMvmetLvlOtherRoom);
			activityDetections.add(movementLvl);
			
			activityLevel.setActivityDetections(activityDetections);
			activityLevel.setActivityLevelExpress(activityLevelExpress);
			
			List<StepCountAvailableReport> stepCountAvailableReports = new ArrayList<StepCountAvailableReport>();
			
			if(stepCountReports != null) {
				for(StepCountAvailableReportPrsr stepCountAvailableReport : stepCountReports) {
					stepCountAvailableReports.add(stepCountAvailableReport.parse());
				}
			}
			
			activityLevel.setStepCountAvailableReport(stepCountAvailableReports);
			
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
		
		return activityLevel;
	}


	public Boolean getbIsAtHome() {
		return bIsAtHome;
	}


	public void setbIsAtHome(Boolean bIsAtHome) {
		this.bIsAtHome = bIsAtHome;
	}


	public String getsLastLocation() {
		return sLastLocation;
	}


	public void setsLastLocation(String sLastLocation) {
		this.sLastLocation = sLastLocation;
	}


	public String getdLocationTime() {
		return dLocationTime;
	}


	public void setdLocationTime(String dLocationTime) {
		this.dLocationTime = dLocationTime;
	}


	public float getfMvmetLvl() {
		return fMvmetLvl;
	}


	public void setfMvmetLvl(float fMvmetLvl) {
		this.fMvmetLvl = fMvmetLvl;
	}


	public int getiMvmetLvlDining() {
		return iMvmetLvlDining;
	}


	public void setiMvmetLvlDining(int iMvmetLvlDining) {
		this.iMvmetLvlDining = iMvmetLvlDining;
	}


	public int getiMvmetLvlBedroom() {
		return iMvmetLvlBedroom;
	}


	public void setiMvmetLvlBedroom(int iMvmetLvlBedroom) {
		this.iMvmetLvlBedroom = iMvmetLvlBedroom;
	}


	public int getiMvmetLvlToilet() {
		return iMvmetLvlToilet;
	}


	public void setiMvmetLvlToilet(int iMvmetLvlToilet) {
		this.iMvmetLvlToilet = iMvmetLvlToilet;
	}


	public int getiMvmetLvlBathroom() {
		return iMvmetLvlBathroom;
	}


	public void setiMvmetLvlBathroom(int iMvmetLvlBathroom) {
		this.iMvmetLvlBathroom = iMvmetLvlBathroom;
	}


	public int getiMvmetLvlLivingRoom() {
		return iMvmetLvlLivingRoom;
	}


	public void setiMvmetLvlLivingRoom(int iMvmetLvlLivingRoom) {
		this.iMvmetLvlLivingRoom = iMvmetLvlLivingRoom;
	}


	public String getsLocationLbl() {
		return sLocationLbl;
	}


	public void setsLocationLbl(String sLocationLbl) {
		this.sLocationLbl = sLocationLbl;
	}


	public int getiMvmetLvlOtherRoom() {
		return iMvmetLvlOtherRoom;
	}


	public void setiMvmetLvlOtherRoom(int iMvmetLvlOtherRoom) {
		this.iMvmetLvlOtherRoom = iMvmetLvlOtherRoom;
	}

	@JsonProperty("ActivityLevel")
	public int getActivityLevelExpress() {
		return activityLevelExpress;
	}

	@JsonProperty("ActivityLevel")
	public void setActivityLevelExpress(int activityLevelExpress) {
		this.activityLevelExpress = activityLevelExpress;
	}


	public List<StepCountAvailableReportPrsr> getStepCountReports() {
		return stepCountReports;
	}


	public void setStepCountReports(List<StepCountAvailableReportPrsr> stepCountReports) {
		this.stepCountReports = stepCountReports;
	}

	
}
