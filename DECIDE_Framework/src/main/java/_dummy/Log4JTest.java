package _dummy;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JTest {

    private final Logger logger = LogManager.getLogger(Log4JTest.class);

    public Log4JTest(String serialPortName) {
    	
    	System.out.println("Looking for configuration on classpath:");
        URL resource = ClassLoader.getSystemResource("log4j2.xml"); 
        System.out.println("found "+String.valueOf(resource));


        
        System.out.println(logger.isInfoEnabled());
        logger.traceEntry();
        logger.info("info! {}", serialPortName);
        logger.error("error! {}", serialPortName);
        logger.debug("debug! {}", serialPortName);
        logger.error("Opening serial port {}", serialPortName);
    }

    public static void main(String args[]) {
    	Log4JTest h1 = new Log4JTest("1001");
    }

}