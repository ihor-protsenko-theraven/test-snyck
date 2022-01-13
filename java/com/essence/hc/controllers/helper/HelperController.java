package com.essence.hc.controllers.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class HelperController {

	/**
	 * This method returns true if the current request is asynchronous, otherwise false 
	 * @return boolean
	 */
	public static boolean isAjax() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
	
	/**
	 * create redirect with redirectFlashAttributes
	 */
	public static String redirectWithFlashAttributes(RedirectAttributes redirectAttributes, String url, String messageCode, String messageTitleCode,String messageArguments) {
		return redirectWithFlashAttributes(redirectAttributes, url, messageCode, messageTitleCode, messageArguments, false);
	}
	
	public static String redirectWithFlashAttributes(RedirectAttributes redirectAttributes, String url, String messageCode, String messageTitleCode, String messageArguments, boolean error) {
		
		redirectAttributes.addFlashAttribute("flashTitleCode", messageTitleCode);
		redirectAttributes.addFlashAttribute("flashMessageCode", messageCode);
		redirectAttributes.addFlashAttribute("flashMessageArgument", messageArguments);
		redirectAttributes.addFlashAttribute("flashError", error);
		
		return url;
	}
	
	/**
	 * render modal response view
	 * requires that messageCode and messageTitleCode 
	 * are defined in i18 resources languages
	 * @param model
	 * @param messageCode
	 * @param messageTitleCode
	 * @return
	 */
	public static String successResponseView(ModelMap model, String messageCode, String messageTitleCode) {
		model.addAttribute("messageCode", messageCode);
		model.addAttribute("messageTitleCode", messageTitleCode);
		model.addAttribute("error", false);
		
		return "adminRequestSuccess";
	}
	
	public static String errorResponseView(ModelMap model, String messageCode, String messageTitleCode) {
		model.addAttribute("messageCode", messageCode);
		model.addAttribute("messageTitleCode", messageTitleCode);
		model.addAttribute("error", true);
		
		return "adminRequestSuccess";
	}
}
