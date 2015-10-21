package robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import decide.configuration.Configuration;

/**
 * class that represents a data structure for storing an RQVResult 
 * <b>this is application specific</b>
 * @author sgerasimou
 *
 */

public class RobotConfiguration extends Configuration{
	private int		CSC;
	private int 	sensor1;
	private int		sensor2;
	private int		sensor3;
	private double 	speed;
	
	
	/**
	 * Class constructor: create a new ResultQV instance
	 * @param CSC
	 * @param speed
	 * @param req1Result
	 * @param req2Result
	 */
	public RobotConfiguration (int CSC, double speed){
		this.CSC					= CSC;
		this.sensor1 				= CSC%2;
		this.sensor2 				= CSC%4>1 ? 1 : 0;
		this.sensor3 				= CSC%8>3 ? 1 : 0;
		this.speed   				= speed;	
		
		//TODO init verification results list
		this.verificationResults 	= new ArrayList<Double>();
	}
	
	
	/**
	 * Print the details of this ResultQV instance
	 */
	@Override
	public String toString() {
//	    String NEW_LINE = System.getProperty("line.separator");
		StringBuilder str = new StringBuilder("[");
	    str.append(sensor1 + ",");
	    str.append(sensor2 + ",");
	    str.append(sensor3 + ",");
	    str.append(speed   + "]");
	    str.append("\t[" + Arrays.toString(verificationResults.toArray()) +"]");
		return str.toString();
	}
	
	
	
	
    /**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseModel(Object ... parameters){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Congifuration Variables\n");

    	//process the given parameters
//		model.append("const double r1  = "+ parameters[0].toString() +";\n");
//		model.append("const double r2  = "+ parameters[1].toString() +";\n");
//		model.append("const double r3  = "+ parameters[2].toString() +";\n");
		model.append("const double p1  = "+ parameters[0].toString() +";\n");
		model.append("const double p2  = "+ parameters[1].toString() +";\n");
		model.append("const double p3  = "+ parameters[2].toString() +";\n");
		model.append("const int    PSC = "+ parameters[3].toString() +";\n");
		model.append("const int    CSC = "+ parameters[4].toString() +";\n");
		model.append("const double s   = "+ parameters[5].toString() +";\n\n");
    	
    	return model.toString();
    }
    
    
	/**
	 * Estimate Probability of producing a successful measurement
	 * @param speed
	 * @param alpha
	 * @return
	 */
	private static double estimateP(double speed, double alpha){
		return 100 - alpha * speed;
	}


	@Override
	public String getModel() {
		
		return realiseModel(
//						5, 4, 4,					// environment information
						estimateP(speed/10.0, 5),	//sensor 1 accuracy
						estimateP(speed/10.0, 7),	//sensor 2 accuracy
						estimateP(speed/10.0, 11),	//sensor 3 accuracy
						5, CSC, speed
					);	
	}


	
	@Override
	public List<?> getConfigurationElements() {
		return Arrays.asList(new Object[]{CSC, sensor1, sensor2, sensor3, speed});
	}
	
	
	public Object getMeasurements(){
		return this.verificationResults.get(0);
	}

	public Object getEnergy(){
		return this.verificationResults.get(1);
	}
	
	public boolean areSensorsOn(){
		return CSC > 0 ? true : false;
	}
	
	public double getSpeed(){
		return this.speed;
	}

	
	
	
}