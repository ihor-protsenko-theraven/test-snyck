package com.essence.hc.web.tags;

public class SvgNotificationIconTag extends SvgIconTagBase {

	@Override
	public String getSvgPath() {
		String svgPath = baseSvgPath; 
		
		if ( cssClass == null ) setCssClass("icon-notification");
		
		if ( iconType != null  ) {
			if ( iconType.equalsIgnoreCase("PHOTO") ) {
				svgPath += "#VIDEO";
			} else if ( iconType.equalsIgnoreCase("EQUIPMENT") ) {
				svgPath += "#TECHNICAL";
			} else if ( iconType.equalsIgnoreCase("USER") ||
						iconType.equalsIgnoreCase("bGetFall") ) {
				svgPath += "#FALL";
			} else if ( iconType.equalsIgnoreCase("bGetSmoke") ) {
				svgPath += "#FIRE";
			} else if ( iconType.equalsIgnoreCase("bGetPanic") ) {
				svgPath += "#SOS";
			} else if ( iconType.equalsIgnoreCase("bGetWtLkge") ) {
				svgPath += "#FLOOD";
			} else if ( iconType.equalsIgnoreCase("bGetNtAtHm") ) {
				svgPath += "#FRONT_DOOR";
			} else if ( iconType.equalsIgnoreCase("bGetActvt") ) {
				svgPath += "#ACTIVITY";
			} else if ( iconType.equalsIgnoreCase("bGetDoor") ) {
				svgPath += "#DOOR";
			} else {
				svgPath += "#" + iconType.toUpperCase();
			}
		}
		return svgPath;
	}

}
