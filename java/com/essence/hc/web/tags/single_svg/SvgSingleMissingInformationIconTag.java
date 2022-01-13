package com.essence.hc.web.tags.single_svg;

public class SvgSingleMissingInformationIconTag extends SvgSingleTagBase {
	
	@Override
	public String getCssClass() {
		String cssClass = "icon-missinginfo";
		if ( this.getIconType().equals("start")) {
			return cssClass + " start";
		} else {
			return cssClass + " end";
		}
	}
	
	@Override
	protected String obtainIconName(String iconType) {
		return iconType.toLowerCase();
	}
	
	@Override
	protected String getIconFolder() {
		return "reports-icons/missing-information/";
	}

}
