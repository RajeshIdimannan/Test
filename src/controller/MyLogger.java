package controller;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;

public class MyLogger {

	static final Logger logger = Logger.getLogger("MyLogger.class");
	MyLogger(){
		try {
			FileHandler fh = new FileHandler("loggerExample.log", true);
			fh.flush();
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			
			e.printStackTrace();						
		};
		
	}
	

	
	
}
