package com.essence.hc.web.tags.single_svg;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SvgSingleNotificationIconTag extends SimpleTagSupport{

	protected String iconType;
	protected String id;
	protected String cssClass;

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSvgPath() {
		
		// TODO ADL ROOTH PATH EXTERNAL ON JSP OR GET FROM SESSION VARIABLE OR STH LIKE THAT
		String svgPath = "/ADL/stylesheets/svg/" + "notification-types-icons/"; 
		svgPath += this.obtainIconName(iconType) + ".svg";
			
		return svgPath;
	}
	
	private String obtainIconName(String iconType){
		
		String iconName = null;
		
		if ( iconType != null  ) {
			if ( iconType.equalsIgnoreCase("PHOTO") ) {
				iconName = "VIDEO";
			} else if ( iconType.equalsIgnoreCase("EQUIPMENT") ) {
				iconName = "TECHNICAL";
			} else if ( iconType.equalsIgnoreCase("USER") ||
						iconType.equalsIgnoreCase("bGetFall") ) {
				iconName = "FALL";
			} else if ( iconType.equalsIgnoreCase("bGetSmoke") ) {
				iconName = "FIRE";
			} else if ( iconType.equalsIgnoreCase("bGetPanic") ) {
				iconName = "SOS";
			} else if ( iconType.equalsIgnoreCase("bGetWtLkge") ) {
				iconName = "FLOOD";
			} else if ( iconType.equalsIgnoreCase("bGetNtAtHm") ) {
				iconName += "FRONT_DOOR";
			} else if ( iconType.equalsIgnoreCase("bGetActvt") ) {
				iconName = "ACTIVITY";
			} else if ( iconType.equalsIgnoreCase("bGetDoor") ) {
				iconName = "DOOR";
			} else {
				iconName = iconType;
			}
		}
		
		return iconName.toLowerCase();
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		renderTag( getSvgPath() );
	}
	
	public void renderTag(String svgPath) throws JspException, IOException {
		final JspWriter writer = getJspContext().getOut();
		
		writer.print("<img class='icon");
		if (cssClass != null) writer.print(" " + cssClass);
		writer.print("'");
		if (id != null) writer.print(" id='" + id + "'");
		if (svgPath != null) writer.print("src=\"" + svgPath + "\"");
		writer.print("/>");
	}
	
}
