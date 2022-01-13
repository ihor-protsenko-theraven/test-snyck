package com.essence.hc.eil.parsers;

import java.util.List;

public class LowActivityRGroupPrsr {

		
	private boolean bValid;
	List<LowActivityRGInfoPrsr> pInfGroup;
	private String dGrade;
	
	public LowActivityRGroupPrsr() {
	}
	
	public boolean isbValid() {
		return bValid;
	}
	public void setbValid(boolean bValid) {
		this.bValid = bValid;
	}
	public List<LowActivityRGInfoPrsr> getpInfGroup() {
		return pInfGroup;
	}
	public void setpInfGroup(List<LowActivityRGInfoPrsr> pInfGroup) {
		this.pInfGroup = pInfGroup;
	}
	public String getdGrade() {
		return dGrade;
	}
	public void setdGrade(String dGrade) {
		this.dGrade = dGrade;
	}

}
