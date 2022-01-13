package com.essence.hc.eil.errors.impl;

import com.essence.hc.eil.errors.ErrorHelper;

public class UpdateUserErrorHelper implements ErrorHelper
{
     @Override
	 public String getErrorMessage(int errorCode) 
	 {
		  String errorMessage;
		 
		  switch (errorCode) 
		  {
			 	case -3:
			 		 errorMessage = "admin.users.duplicate";
			 		 break;
			 	case -5:
			 		 errorMessage = "ERROR_SERIAL_NUMBER_IN_USE";
			 		 break;
			 	case -6:
			 		 errorMessage =  "ERROR_ACCOUNT_NUMBER_IN_USE";
			 		 break;
			 	case -9:
			 	case 134:
			 		 errorMessage = "ERROR_EMAIL_IN_USE";
			 		 break;
			 	case 6:
			 		 errorMessage = "admin.users.error.not_assigned_resident";
			 		 break;
			 	case 10:
			 		 errorMessage = "admin.users.error.saving_database";
			 		 break;
			 	case 30:
			 		 errorMessage = "User not authorized for this action";
			 		 break;
			 	case 66:
	                 errorMessage = "admin.users.error.cellphone";
                     break;
			 	case 124:
			 		 errorMessage = "admin.users.error.not_existing_account";
			 		 break;
			 	case 129:
			 		 errorMessage = "ERROR_ACCOUNT_NUMBER_IN_USE";
			 		 break;
			 	case 141:
			 		 errorMessage = "admin.users.error.timezone_not_supported";
			 		 break;
			 	case 142:
			 		 errorMessage = "admin.users.error.control_panel_failed_update_timezone";
			 		 break;
			 	case -167:
	    			errorMessage = "xss.error";
	    	  		break;
	    		case 167:
	    	  		 errorMessage = "xss.error";
	    	  		 break;
	    		case 121:
	    		case 171:
	    			errorMessage = "admin.users.error.phone_number";
	    			break;
	    		case 173:
	    		case 174:
	    			errorMessage = "TeleHealth service is not supported";
	    			break;
			 	default:
			 		 errorMessage = "admin.users.disable.fail.message";
			 		 break;
		  }
		  
		  return errorMessage;
	 }
}
