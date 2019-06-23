package caseStudies.activityBot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import decide.configuration.Configuration;

/**
 * class that represents a data structure for storing an RQVResult 
 * <b>this is application specific</b>
 * @author syonbawi
 *
 */

public class ActivityBotConfiguration extends Configuration{
	private double		x;
	private int 		speed;
	private double		cost;	
	private double 		task1Rate;
	private double 		task2Rate;
	private double 		task3Rate;
	
	
	/**
	 * Class constructor: create a new ResultQV instance
	 * @param CSC
	 * @param speed
	 */
	public ActivityBotConfiguration (double x, int speed, double cost, double r1, double r2, double r3){
		this.x					= x;
		this.speed				= speed;
		this.cost				= cost;
		this.task1Rate			=r1;
		this.task2Rate			=r2;
		this.task3Rate			=r3;
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
		 str.append(x + ",");
		str.append(speed + ",");
	    str.append(cost + "]");
	    if(verificationResults!=null)
	    str.append("\t[" + Arrays.toString(verificationResults.toArray()));
	    str.append( "]");
		return str.toString();
	}
 


	@Override
	public String getModel() {
		
		return realiseModel(
//				5, 4, 4,					// environment information
				task1Rate,				//rate of task 1
				task2Rate,				//rate of task 2
				task3Rate,				//rate of task 3
				x, speed, cost			// control variable x, robot speed, speed cost 
			);	
	}

	/**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseModel(Object ... parameters){
    	StringBuilder model = new StringBuilder(modelTemplate + "\n\n//Congifuration Variables\n");

    	//process the given parameters
		model.append("const double r1  = "+ parameters[0].toString() +";\n");
		model.append("const double r2  = "+ parameters[1].toString() +";\n");
		model.append("const double r3  = "+ parameters[2].toString() +";\n");
		model.append("const double p_i2  = "+ parameters[6].toString() +";\n");
		model.append("const double p_i3  = "+ parameters[7].toString() +";\n");
		model.append("const double x  = "+ parameters[3].toString() +";\n");
		model.append("const double s = "+ parameters[4].toString() +";\n");
		model.append("const double ENERGY_COST = "+ parameters[5].toString() +";\n");
		//model.append("const double s   = "+ parameters[5].toString() +";\n\n");
    	
    	return model.toString();
    }

	
	@Override
	public List<?> getConfigurationElements() {
		return Arrays.asList(new Object[]{x, speed, cost});
	}
	
	
	public Object getContributionTime(){
		return this.verificationResults.get(0);
	}

	public Object getUtility(){
		return this.verificationResults.get(1);
	}
	
	public Object getCost(){
		return this.verificationResults.get(2);
	}
	
	
	
	


	@Override
	public double getBound() {
		return (double)getUtility();
	}

	
	
	
}