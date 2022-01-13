package com.essence.hc.eil.parsers;

import com.essence.hc.model.Activity.ActivityType;

public class LowActivityRGInfoPrsr {


	private String iReportRuleId;
	private String iReportRuleGrd;
	private String sReportRuleDt;
	private String sReportRuleLbl;
	private String sReportRuleVal;
	private String sReportRuleInf;
	private String sReportPriodLbl;
	private String sReportPriodHrs; // time interval (hours)
	private ActivityType sReportActvtLbl;
	private String sSvrt;
	
	
	public LowActivityRGInfoPrsr() {
	}
	
	public String getiReportRuleId() {
		return iReportRuleId;
	}
	public void setiReportRuleId(String iReportRuleId) {
		this.iReportRuleId = iReportRuleId;
	}
	public String getiReportRuleGrd() {
		return iReportRuleGrd;
	}
	public void setiReportRuleGrd(String iReportRuleGrd) {
		this.iReportRuleGrd = iReportRuleGrd;
	}
	public String getsReportRuleDt() {
		return sReportRuleDt;
	}
	public void setsReportRuleDt(String sReportRuleDt) {
		this.sReportRuleDt = sReportRuleDt;
	}
	public String getsReportRuleLbl() {
		return sReportRuleLbl;
	}
	public void setsReportRuleLbl(String sReportRuleLbl) {
		this.sReportRuleLbl = sReportRuleLbl;
	}
	public String getsReportRuleVal() {
		return sReportRuleVal;
	}
	public void setsReportRuleVal(String sReportRuleVal) {
		this.sReportRuleVal = sReportRuleVal;
	}
	public String getsReportRuleInf() {
		return sReportRuleInf;
	}
	public void setsReportRuleInf(String sReportRuleInf) {
		this.sReportRuleInf = sReportRuleInf;
	}
	public String getsReportPriodLbl() {
		return sReportPriodLbl;
	}
	public void setsReportPriodLbl(String sReportPriodLbl) {
		this.sReportPriodLbl = sReportPriodLbl;
	}
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
