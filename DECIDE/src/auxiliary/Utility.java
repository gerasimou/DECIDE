package auxiliary;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Utility {

	// assumes the current class is called logger
	private final static Logger logger = Logger.getLogger(Logger.class);

	private final static String fileName = "resources/config.properties";
	private static Properties properties;
	
	/** Setup Utility class*/
	public static void setup(){
		loadPropertiesInstance();	
		getProperty("");
	}
	
	
	/**
	 * Load Properties instance  
	 */
	private static void loadPropertiesInstance(){
		try {
			if (properties == null){
				properties = new Properties();
				properties.load(new FileInputStream(fileName));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** Get the value of the key property if exist, otherwise throw an <b>IllegalArgumentException</b>
	 * @param key String
	 * @return String value
	 * @throws IllegalArgumentException
	 */
	public static String getProperty (String key){
		String result = properties.getProperty(key); 
		if (result == null)
			  throw new IllegalArgumentException(key.toUpperCase() + " name not found!");
		return result;		
	}
	
	
	public static String getProperty (String key, String defaultValue){
		String output = properties.getProperty(key);
		return (output != null ? output : defaultValue);
	}
	
	
	public static void exportToFile(String fileName, String output){
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.append(output +"\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void createFileAndExport(String inputFileName, String outputFileName, String outputStr){
		FileChannel inputChannel 	= null;
		FileChannel outputChannel	= null;
				
		try {
			File input 	= new File(inputFileName);
			File output 	= new File(outputFileName);
			
			inputChannel 	= new FileInputStream(input).getChannel();
			outputChannel	= new FileOutputStream(output).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

			inputChannel.close();
			outputChannel.close();
			
			exportToFile(outputFileName, outputStr);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void log(String parameter){
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + parameter);
		}
		
		if(logger.isInfoEnabled()){
			logger.info("This is info : " + parameter);
		}
		
		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);
		

	}
}
