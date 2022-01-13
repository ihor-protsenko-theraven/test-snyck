package com.essence.hc.web.tags.single_svg;

public class SvgSingleEPADeviceStatusTag extends SvgSingleTagBase {
		
	@Override
	public String getCssClass() {
		String cssClass = "icon-epa-status";
		if ( this.cssClass != null ) cssClass += " " + this.cssClass;
		
		return cssClass;
	}
	
	@Override
	protected String obtainIconName(String iconType) {
		return iconType.toLowerCase();
	}
	
	@Override
	protected String getIconFolder() {
		return "epa-status/";
	}
}
