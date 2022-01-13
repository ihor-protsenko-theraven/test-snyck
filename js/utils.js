//12-24H CLOCK OPERATIONS
function formatDateTime(dateTime, timeFormat)
{
	var formattedTime;
	
	if (timeFormat.indexOf("H") >= 0) 
	{
         formattedTime = dateTime;
    }
	else
	{
		var day = dateTime.slice(0, 2);
    	var month = dateTime.slice(3, 5);
    	var year = dateTime.slice(6, 10);
    	var hour = dateTime.slice(11,13);
    	var minute = dateTime.slice(14,16);
    	
    	if (hour>12)
    	     formattedTime = month+"/"+day+"/"+year+" "+zp(hour-12, 2)+":"+minute+" PM";
    	else if (hour==0)
    		 formattedTime = month+"/"+day+"/"+year+" 12:"+minute+" AM";
    	else if (hour==12)
    		 formattedTime = month+"/"+day+"/"+year+" 12:"+minute+" PM";
    	else
   		     formattedTime = month+"/"+day+"/"+year+" "+hour+":"+minute+" AM";
	}
  
	return formattedTime;
}

function formatTime(time, timeFormat)
{
	var formattedTime;
	
	if (timeFormat.indexOf("H") >= 0) 
	{
         formattedTime = time;
	}
	else
    {
		 var hour = time.slice(0, 2);
     	 var minute = time.slice(3, 5);
    	
    	 if (hour>12)
    	     formattedTime = zp(hour-12, 2)+":"+minute+" PM";
    	 else if (hour==0)
    		 formattedTime = "12:"+minute+" AM";
    	 else if (hour==12)
    		 formattedTime = "12:"+minute+" PM";
    	 else
   		     formattedTime = hour+":"+minute+" AM";
    }
    
	return formattedTime;
}

var zp = function(n,c) { 
	var s = String(n); 
	if (s.length< c) { return zp("0" + n,c) } 
	else { return s } 
}

