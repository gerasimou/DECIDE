package caseStudies.uuv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import decide.configuration.Configuration;
import decide.localAnalysis.LocalCapabilityAnalysis;

/**
 * class that represents a data structure for storing an RQVResult 
 * <b>this is application specific</b>
 * @author sgerasimou
 * 
 *
 */

public class UUVConfiguration extends Configuration{
	private int		CSC;
	private int 	sensor1;
	private int		sensor2;
	private int		sensor3;
	private double 	speed;
	private double 	sensor1Probability;
	private double 	sensor2Probability;
	private double 	sensor3Probability;
	
	
	/**
	 * Class constructor: create a new ResultQV instance
	 * @param CSC
	 * @param speed
	 * @param req1Result
	 * @param req2Result
	 */
	public UUVConfiguration (int CSC, double speed){
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
		StringBuilder str = new StringBuilder();
	    str.append(sensor1 + ",");
	    str.append(sensor2 + ",");
	    str.append(sensor3 + ",");
	    str.append(speed   + ",");
	    str.append(Arrays.toString(verificationResults.toArray()));
		return str.toString();
	}
	
	
	
	
    /**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseModel(Object ... parameters){
    	StringBuilder model = new StringBuilder(modelTemplate + "\n\n//Congifuration Variables\n");

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
	 * Evaluate sensor accuracy
	 * @return boolean
	 */
	public boolean evaluateSensorAccuracy(int constraint){
		boolean result = false;
		if (CSC==1 || CSC==2 || CSC==4 || CSC==0){
			switch(CSC)
			{
			case 1: result = sensor1Probability > constraint ? true: false;
			break;
			case 2:	result = sensor2Probability > constraint ? true: false;
			break;
			case 4:	result = sensor3Probability > constraint ? true: false;
			break;
			case 0:	result = true;
			break;
			//default: return result;
			}
		}
		else if (CSC==3 || CSC==5 || CSC==6){
			switch(CSC)
			{
			case 3: result =  (sensor1Probability+sensor2Probability)/2 > constraint ? true: false;
			break;
			case 5:	result =  (sensor1Probability+sensor3Probability)/2 > constraint ? true: false;
			break;
			case 6:	result =  (sensor2Probability+sensor3Probability)/2 > constraint ? true: false;
			break;
			//default: return result;
			}
		}
		else if (CSC==7){
			result =  (sensor1Probability+sensor2Probability+sensor3Probability)/3 > constraint ? true: false;
		}
		
		return result;
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
//						5, 4, 4,								// environment information
				sensor1Probability=estimateP(speed/10.0, 5),	//sensor 1 accuracy
				sensor2Probability=estimateP(speed/10.0, 7),	//sensor 2 accuracy
				sensor3Probability=estimateP(speed/10.0, 11),	//sensor 3 accuracy
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

	
	public int getCSC() {
		return CSC;
	}

	
	@Override
	public double getBound() {
		return (double)getMeasurements();
	}
	
	
 	public double getLocalBound() {
		return (double)this.getLocalRequirementsResults().get("R6");
		
	}
 	
}