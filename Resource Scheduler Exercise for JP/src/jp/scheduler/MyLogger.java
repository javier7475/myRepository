package jp.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Class for logging 
public class MyLogger {
	
	public static Logger log=null;
	
	public static Logger getLog() {
		if (log==null)
			log=LogManager.getLogger("schedulerlog");
		
		return log;
	}
}
