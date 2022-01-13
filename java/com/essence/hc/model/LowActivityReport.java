package com.essence.hc.model;

import java.util.List;

import com.essence.hc.model.Activity.ActivityType;

/**
 * Low Activity Report Class
 * Detailed information for 'Low Activity' kind of Alerts
 *  
 * @author oscar.canalejo
 *
 */
public class LowActivityReport {

	private int alertId;
	private List<ReportGroup> repGroups;

	
	
	public int getAlertId() {
		return alertId;
	}
	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}
	public List<ReportGroup> getRepGroups() {
		return repGroups;
	}
	public void setRepGroups(List<ReportGroup> repGroups) {
		this.repGroups = repGroups;
	}	
	
	
	public class ReportGroup {
		
//		private boolean bValid;
		List<ReportGroupInfo> groupInfoList;
//		private String dGrade;
		
		
//		public boolean isbValid() {
//			return bValid;
//		}
//		public void setbValid(boolean bValid) {
//			this.bValid = bValid;
//		}
//		public String getdGrade() {
//			return dGrade;
//		}
//		public void setdGrade(String dGrade) {
//			this.dGrade = dGrade;
//		}
		public List<ReportGroupInfo> getGroupInfoList() {
			return groupInfoList;
		}
		public void setGroupInfoList(List<ReportGroupInfo> groupInfoList) {
			this.groupInfoList = groupInfoList;
		}
	

	}
	
	public class ReportGroupInfo {
		
//		private String iReportRuleId;
//		private String iReportRuleGrd;
//		private String sReportRuleDt;
		private String sReportRuleLbl; // * sReportRuleLbl + "-" + sReportRuleInf  
//		private String sReportRuleVal;
		private String sReportRuleInf;// *
//		private String sReportPriodLbl;
		private String sReportPriodHrs; // time interval (hours) *
		private ActivityType sReportActvtLbl; // * type (icon)
		private String sSvrt; // * (Line color)
		
		
//		public String getiReportRuleId() {
//			return iReportRuleId;
//		}
//		public void setiReportRuleId(String iReportRuleId) {
//			this.iReportRuleId = iReportRuleId;
//		}
//		public String getiReportRuleGrd() {
//			return iReportRuleGrd;
//		}
//		public void setiReportRuleGrd(String iReportRuleGrd) {
//			this.iReportRuleGrd = iReportRuleGrd;
//		}
//		public String getsReportRuleDt() {
//			return sReportRuleDt;
//		}
//		public void setsReportRuleDt(String sReportRuleDt) {
//			this.sReportRuleDt = sReportRuleDt;
//		}
		public String getsReportRuleLbl() {
			return sReportRuleLbl;
		}
		public void setsReportRuleLbl(String sReportRuleLbl) {
			this.sReportRuleLbl = sReportRuleLbl;
		}
//		public String getsReportRuleVal() {
//			return sReportRuleVal;
//		}
//		public void setsReportRuleVal(String sReportRuleVal) {
//			this.sReportRuleVal = sReportRuleVal;
//		}
		public String getsReportRuleInf() {
			return sReportRuleInf;
		}
		public void setsReportRuleInf(String sReportRuleInf) {
			this.sReportRuleInf = sReportRuleInf;
		}
//		public String getsReportPriodLbl() {
//			return sReportPriodLbl;
//		}
//		public void setsReportPriodLbl(String sReportPriodLbl) {
//			this.sReportPriodLbl = sReportPriodLbl;
//		}
		public String getsReportPriodHrs() {
			return sReportPriodHrs;
		}
		public void setsReportPriodHrs(String sReportPriodHrs) {
			this.sReportPriodHrs = sReportPriodHrs;
		}
		public ActivityType getsReportActvtLbl() {
			return sReportActvtLbl;
		}
		public void setsReportActvtLbl(ActivityType sReportActvtLbl) {
			this.sReportActvtLbl = sReportActvtLbl;
		}
		public String getsSvrt() {
			return sSvrt;
		}
		public void setsSvrt(String sSvrt) {
			this.sSvrt = sSvrt;
		}

	}	

}
