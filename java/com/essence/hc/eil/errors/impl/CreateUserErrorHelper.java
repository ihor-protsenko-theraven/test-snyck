package com.essence.hc.eil.errors.impl;

import com.essence.hc.eil.errors.ErrorHelper;

public class CreateUserErrorHelper implements ErrorHelper
{
	  @Override
      public String getErrorMessage(int errorCode) 
      {
    	  String errorMessage;
    	  
    	  switch (errorCode) 
    	  {
	    	  	case -2:
	    	  		 errorMessage = "admin.users.duplicate";
	    	  		 break;
	    	  	case -5:
	    	  		 errorMessage = "ERROR_SERIAL_NUMBER_IN_USE";
	    	  		 break;
	    	  	case -6:
	    	  	case -10:
	    	  		errorMessage = "ERROR_ACCOUNT_NUMBER_IN_USE";
	    	  		break;
	    	  	case 27:
	    	  		 errorMessage = "admin.users.duplicate";
	    	  		 break;
	    	  	case 66:
	                 errorMessage = "admin.users.error.cellphone";
                     break;
	    	  	case -9:
	    	  	case 134:
	    	  		 errorMessage = "ERROR_EMAIL_IN_USE";
	    	  		 break;
	    	  	case 138:
	    	  		 errorMessage = "admin.users.error.servicepackagedoesnotexist";
	    	  		 break;
	    	  	case 139:
	    	  		 errorMessage = "admin.users.error.servicepackagenotsupportedbypanel";
	    	  		 break;
	    	  	case 147:
	    	  		 errorMessage = "admin.users.error.servicepackagenotenabled";
	    	  		 break;
	    	  	case 112:
	    	  		errorMessage = "admin.users.error.passwordstrength";
	    	  		break;
	    		case 113:
	    	  		errorMessage = "admin.users.error.passwordhistory";
	    	  		break;
	    		case 30:
	    	  		errorMessage = "admin.users.error.passparam_wrong";
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
			  case 181:
				  errorMessage= "admin.users.error.phone_number.automatically_retrieved";
				  break;
	    	  	default:
	    	  		errorMessage = "admin.users.disable.fail.message";
	    	  		 break;
    	   }
    	  
    	   return errorMessage;
	  }
}
