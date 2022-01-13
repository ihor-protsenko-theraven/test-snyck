package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.LowActivityReport;


@JsonIgnoreProperties(ignoreUnknown = true)
public class LowActivityPrsr implements IParser<LowActivityReport> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private boolean bValid;
	private List<LowActivityRGroupPrsr> pRepGrp;

	private String iSnstvt;
	private String iAlrtCount;
	private String iMaxRulGrd;
	private String iCntRulGrd;
	private String dAvgGrd;
	private String dTtlGrade;
	
	
	public LowActivityPrsr() {
	}


	@Override
	public LowActivityReport parse() {
		
		LowActivityReport laReport = new LowActivityReport();
		try{
			List<LowActivityReport.ReportGroup> targetGroupList = new ArrayList<LowActivityReport.ReportGroup>();
			/*
			 * Group elements
			 */
			for(LowActivityRGroupPrsr group : this.pRepGrp){
				LowActivityReport.ReportGroup targetGroup = laReport.new ReportGroup();
				
				if (group.pInfGroup != null && group.pInfGroup.size() > 0) {
					List<LowActivityReport.ReportGroupInfo> targetGroupInfoList = 
							new ArrayList<LowActivityReport.ReportGroupInfo>();
					/*
					 * Group Info elements
					 */
					for(LowActivityRGInfoPrsr info : group.pInfGroup){
						LowActivityReport.ReportGroupInfo targetGroupInfo = laReport.new ReportGroupInfo();
						targetGroupInfo.setsReportActvtLbl(info.getsReportActvtLbl());
						targetGroupInfo.setsReportPriodHrs(info.getsReportPriodHrs());
						targetGroupInfo.setsReportRuleInf(info.getsReportRuleInf());
						targetGroupInfo.setsReportRuleLbl(info.getsReportRuleLbl());
						targetGroupInfo.setsSvrt(info.getsSvrt());
						targetGroupInfoList.add(targetGroupInfo);
					}
					targetGroup.setGroupInfoList(targetGroupInfoList);
				}
				targetGroupList.add(targetGroup);
			}
			laReport.setRepGroups(targetGroupList);
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return laReport;
	}



	public boolean isbValid() {
		return bValid;
	}


	public void setbValid(boolean bValid) {
		this.bValid = bValid;
	}


	public List<LowActivityRGroupPrsr> getpRepGrp() {
		return pRepGrp;
	}


	public void setpRepGrp(List<LowActivityRGroupPrsr> pRepGrp) {
		this.pRepGrp = pRepGrp;
	}


	public String getiSnstvt() {
		return iSnstvt;
	}


	public void setiSnstvt(String iSnstvt) {
		this.iSnstvt = iSnstvt;
	}


	public String getiAlrtCount() {
		return iAlrtCount;
	}


	public void setiAlrtCount(String iAlrtCount) {
		this.iAlrtCount = iAlrtCount;
	}


	public String getiMaxRulGrd() {
		return iMaxRulGrd;
	}


	public void setiMaxRulGrd(String iMaxRulGrd) {
		this.iMaxRulGrd = iMaxRulGrd;
	}


	public String getiCntRulGrd() {
		return iCntRulGrd;
	}


	public void setiCntRulGrd(String iCntRulGrd) {
		this.iCntRulGrd = iCntRulGrd;
	}


	public String getdAvgGrd() {
		return dAvgGrd;
	}


	public void setdAvgGrd(String dAvgGrd) {
		this.dAvgGrd = dAvgGrd;
	}


	public String getdTtlGrade() {
		return dTtlGrade;
	}


	public void setdTtlGrade(String dTtlGrade) {
		this.dTtlGrade = dTtlGrade;
	}
}

