package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.SesLogFrm;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SesLogFrmPrsr implements IParser<SesLogFrm> {


	private String[] sIds;
	private String[] sLinkId;
	private String[] sEvntType;
	private String[] sFuncTxt;
	private String[] sDateTime;
	private String[] sPrm1;
	private String[] sPrm2;
	private String[] sPrm3;
	private String[] sUsrNm;
	private String[] sUsrPic;

	
	
	
	public SesLogFrmPrsr() {
	}


	@Override
	public SesLogFrm parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		SesLogFrm logFrm = new SesLogFrm();	
		try {			
			 logFrm.setsIds(sIds);
			 logFrm.setsLinkId(sLinkId);
			 logFrm.setEvntType(sEvntType);
			 logFrm.setsFuncTxt(sFuncTxt);
			 logFrm.setsDateTime(sDateTime);
			 logFrm.setsPrm1(sPrm1);
			 logFrm.setsPrm2(sPrm2);
			 logFrm.setsPrm3(sPrm3);
			 logFrm.setsUsrNm(sUsrNm);
			 logFrm.setsUsrPic(sUsrPic);
			 
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return logFrm;
	}


	public String[] getsIds() {
		return sIds;
	}


	public void setsIds(String[] sIds) {
		this.sIds = sIds;
	}


	public String[] getsLinkId() {
		return sLinkId;
	}


	public void setsLinkId(String[] sLinkId) {
		this.sLinkId = sLinkId;
	}


	

	public String[] getsFuncTxt() {
		return sFuncTxt;
	}


	public void setsFuncTxt(String[] sFuncTxt) {
		this.sFuncTxt = sFuncTxt;
	}


	public String[] getsDateTime() {
		return sDateTime;
	}


	public void setsDateTime(String[] sDateTime) {
		this.sDateTime = sDateTime;
	}


	public String[] getsPrm1() {
		return sPrm1;
	}


	public void setsPrm1(String[] sPrm1) {
		this.sPrm1 = sPrm1;
	}


	public String[] getsPrm2() {
		return sPrm2;
	}


	public void setsPrm2(String[] sPrm2) {
		this.sPrm2 = sPrm2;
	}


	public String[] getsPrm3() {
		return sPrm3;
	}


	public void setsPrm3(String[] sPrm3) {
		this.sPrm3 = sPrm3;
	}


	public String[] getsUsrNm() {
		return sUsrNm;
	}


	public void setsUsrNm(String[] sUsrNm) {
		this.sUsrNm = sUsrNm;
	}


	public String[] getsUsrPic() {
		return sUsrPic;
	}


	public void setsUsrPic(String[] sUsrPic) {
		this.sUsrPic = sUsrPic;
	}

	 @JsonProperty("EvntType")
	public String[] getsEvntType() {
		return sEvntType;
	}

	 @JsonProperty("EvntType")
	public void setsEvntType(String[] sEvntType) {
		this.sEvntType = sEvntType;
	}

	
}
