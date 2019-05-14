package auxiliary;

// Environment variable DYLD_LIBRARY_PATH = repo/prism
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class Utility {

	// assumes the current class is called logger
	private final static Logger logger = Logger.getLogger(Utility.class);

	private final static String fileName = "resources/uuv/config.properties";
	private static Properties properties;
	
	/** Setup Utility class*/
	public static void setup(){
		loadPropertiesInstance();
		System.setProperty("java.net.preferIPv4Stack" , "true");
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
	
	
	public static Set<Entry<Object, Object>> getPropertiesEntrySet(){
		return properties.entrySet();
	}
	
	
	/**
	 * Clone the current  object using serialisation.
	 * It cannot be applied to classes that do not support serialisation.
	 * @param object current object
	 * @return new Object
	 */
	public static Object deepCopy(Object object){
		try {
		      ByteArrayOutputStream baos	= new ByteArrayOutputStream();
		      ObjectOutputStream oos 		= new ObjectOutputStream(baos);
		      oos.writeObject(object);
		      ByteArrayInputStream bais 	= new ByteArrayInputStream(baos.toByteArray());
		      ObjectInputStream ois 		= new ObjectInputStream(bais);
		      return ois.readObject();    
		}
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	}
	
	
	public static String readFile(String fileName) {
		StringBuilder model = new StringBuilder(100);
		BufferedReader bfr = null;

		try {
			bfr = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
