package com.essence.hc.web.tags.single_svg;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SvgSingleTagBase extends SimpleTagSupport {
	
	protected String iconType;
	protected String id;
	protected String cssClass;
	
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
	
	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	protected String getSvgPath(){
		String svgPath = getRootPath() + "/stylesheets/svg/" + getIconFolder() + obtainIconName(iconType) + ".svg";
		
		return svgPath;
	}
	
	protected String obtainIconName(String iconType) {
		return iconType;
	}
	
	protected String getIconFolder() {
		return "";
	}
	
	protected String getRootPath() {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		return request.getContextPath();
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		renderTag(getSvgPath());
	}
	
	public void renderTag(String svgPath) throws JspException, IOException{
		final JspWriter writter = getJspContext().getOut();
		
		writter.print("<img class='icon");
		if (getClass() != null)
			writter.print(" " + getCssClass());
		writter.print("'");
		if (getId() != null)
			writter.print(" id='" + getId() + "'");
		if (svgPath != null)
			writter.print("src=\"" + svgPath + "\"");
		writter.print("/>");
	}
}
