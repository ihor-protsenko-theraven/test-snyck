package com.essence.hc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	//public static final String FORMAT_STRING = "%1$tF %1$tT.%1$tL - %2$s: %3$s%n";
	public static final String FORMAT_STRING = "%1$tT.%1$tL (%2$d) - %3$s: %4$s %5$s%n";

	public LogFormatter() {
		super();
	}

    public String format(LogRecord record) {
    	long time= record.getMillis();
     	    	
 		String origin = "[" + record.getSourceClassName() + "][" + record.getSourceMethodName() + "]";
     	if (record.getLevel() ==  Level.SEVERE) {
     		String stackTrace = getStackTrace(record.getThrown());
     		return String.format(FORMAT_STRING, time, record.getThreadID(), record.getLevel().getName(), origin, formatMessage(record) +"\n"+ stackTrace );
     	} else
     		return String.format(FORMAT_STRING, time, record.getThreadID(), record.getLevel().getName(), origin, formatMessage(record) );
    }    

    /**
     * Para Mostar el stack de traza en nuestro log
     * @param aThrowable
     * @return
     */
    private static String getStackTrace(Throwable aThrowable) {
    	if  (aThrowable == null)
    		return "";
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
      }
}
