package caseStudies.uuv;

import java.util.Arrays;
import java.util.List;

import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.Configuration;

public class UUVConfiguration extends Configuration {

	private int		CSC;
	private int 	sensor1;
	private int		sensor2;
	private int		sensor3;
	private double 	speed;
	private double 	p1;
	private double 	p2;
	private double 	p3;

	
	
	public UUVConfiguration (DECIDEAttributeCollection attributeCollection, int CSC, double speed){
		super (attributeCollection);
		
		this.CSC		= CSC;
		this.sensor1 	= CSC%2;
		this.sensor2 	= CSC%4>1 ? 1 : 0;
		this.sensor3 	= CSC%8>3 ? 1 : 0;
		this.speed   	= speed;
		
		p1 				= estimateP(speed/10.0, 5);
		p2				= estimateP(speed/10.0, 7);
		p3				= estimateP(speed/10.0, 11);
	}

	
	@Override
	public String getConfigurationModel() {
		StringBuilder model = new StringBuilder("\n\n//Congifuration Variables\n");

		//add configuration parameters
		model.append("const double p1  = "+ p1  +";\n");
		model.append("const double p2  = "+ p2  +";\n");
		model.append("const double p3  = "+ p3 +";\n");
		model.append("const int    PSC = "+ 1 +";\n");
		model.append("const int    CSC = "+ CSC +";\n");
		model.append("const double s   = "+ speed +";\n\n");
	
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

	
	/**
	 * Get the configuration elements that affect the confidence (alpha value) on the environment
	 * If no configurable parameter has any effect on the confidence return null.
	 * For instance, in the UUV case study when two sensors (components) are active the conservative estimation
	 * of the environment parameter is affected analogously
	 */
	@Override
	public List<Object> getConfigurationElements() {
		return Arrays.asList(new Object[]{CSC, sensor1, sensor2, sensor3, speed});
	}

	
	public double getUtility() {
		return (double) localRequirementsResults.get("local-utility");//FIXME: Hardcoded for now
	}
	
	
    /**
	 * Evaluate sensor accuracy
	 * @return boolean
	 */
	public boolean evaluateSensorAccuracy(final double MIN_ACCURACY){
		double result = getSensorAccuracy();
		return result >= MIN_ACCURACY;
	}
	
	
	public double getSensorAccuracy() {
		double result = 0;
		if (CSC==1 || CSC==2 || CSC==4 || CSC==0){
			switch(CSC) {
				case 1: { result = p1; break;}
				case 2:	{ result = p2; break;}
				case 4:	{ result = p3; break;}
				case 0:	{ result = 0; break;}
			}
		}
		else if (CSC==3 || CSC==5 || CSC==6){
			switch(CSC) {
				case 3: { result = (p1+p2)/2; break;}
				case 5:	{ result = (p1+p3)/2; break;}
				case 6:	{ result = (p2+p3)/2; break;}
			}
		}
		else if (CSC==7){
			result =  (p1+p2+p3)/3;
		}
		return result;
	}
	
	
	public double getSpeed(){
		return this.speed;
	}
	
	public int getCSC() {
		return CSC;
	}

}
