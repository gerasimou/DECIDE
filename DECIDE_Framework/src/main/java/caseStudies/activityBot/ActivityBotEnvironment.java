package caseStudies.activityBot;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import caseStudies.uuv.UUVEnvironment;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class ActivityBotEnvironment extends Environment {

	Random rand = new Random(System.currentTimeMillis());
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(ActivityBotEnvironment.class);
    
    double [][] degradationP2;
    double [][] degradationP3;
    
    /** Time stamp to record mission start activity*/
	protected long missionStartTimeStamp;
	
	public ActivityBotEnvironment() {
		initEnvironment();
		setupSimulationParam();
	}
	
	
	protected void initEnvironment(){
		environmentMap.put("p2", Double.parseDouble(Utility.getProperty("p2")) + rand.nextDouble());
		environmentMap.put("p3", Double.parseDouble(Utility.getProperty("p3")) + rand.nextDouble());
	}

	protected void setupSimulationParam()
	{
		try
		{
		// setup mission time synchronization clock
		missionStartTimeStamp = System.currentTimeMillis();
	
		String[] featuresListP2	= (Utility.getProperty("DEGRADATIONP2")).split(",");
		if(featuresListP2[0].length() <1 || featuresListP2 == null)
			logger.info("No simulation input fount for p2");
		else
		{
			degradationP2 = new double [featuresListP2.length][3];
		for(int i=0 ;i<featuresListP2.length; i++)
		{	
			String [] featureSplit = featuresListP2[i].split(":");
			for(int j=0;j<3;j++)
				degradationP2[i][j] = Double.parseDouble(featureSplit[j].replaceAll("\\s+",""));
			
		}
		}// end else
		
		String[] featuresListP3	= (Utility.getProperty("DEGRADATIONP3")).split(",");
		if(featuresListP3[0].length() <1 || featuresListP3 == null)
			logger.info("No simulation input fount for p3");
		else
		{
			degradationP3 = new double [featuresListP3.length][3];
		for(int i=0 ;i<featuresListP3.length; i++)
		{	
			String [] featureSplit = featuresListP3[i].split(":");
			for(int j=0;j<3;j++)
				degradationP3[i][j] = Double.parseDouble(featureSplit[j].replaceAll("\\s+",""));
			
		}
		}// end else
		
		}
		catch(ArrayIndexOutOfBoundsException exception)
		{
			logger.error("UUV degradation indexes not properly formatted", exception);
			System.exit(1);
		}
		catch(Exception exception)
		{
			logger.error("UUV degradation indexes not properly formatted", exception);
			System.exit(1);
		}
	}
	
	protected void simulateEnvironment(){
		double currentTime = (System.currentTimeMillis() - missionStartTimeStamp)/1000;
		logger.info("[SimulateEnvironment]:T="+currentTime);
		
		environmentMap.put("p2", Double.parseDouble(Utility.getProperty("p2")) + (rand.nextDouble()*0.166));//*0.166)+ 0.5);
		environmentMap.put("p3", Double.parseDouble(Utility.getProperty("p3")) + (rand.nextDouble()*0.166));//*0.166)+ 0.5);
		
		if(degradationP2 != null)
		{
			for(int i=0;i<degradationP2.length;i++)
			{
				if(degradationP2[i][0]<=currentTime && currentTime<=degradationP2[i][1])
				{	
					environmentMap.put("p2", degradationP2[i][2]/100 *Double.parseDouble(Utility.getProperty("p2")));
					logger.info("[Degradation "+degradationP2[i][2]+"% in task 2 probability");
					break;
				}
			}
		 
		}
		if(degradationP3 != null)
		{
			for(int i=0;i<degradationP3.length;i++)
			{
				if(degradationP3[i][0]<=currentTime && currentTime<=degradationP3[i][1])
				{	
					environmentMap.put("p3", degradationP3[i][2]/100 *Double.parseDouble(Utility.getProperty("p3")));
					logger.info("[Degradation "+degradationP3[i][2]+"% in task 3 probability");
					break;
				}
			}
		 
		}
		
	}

	@Override
	public String getModel() {
		StringBuilder model = new StringBuilder("\n\n//Environment Variables\n");

		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			model.append("const double " + entry.getKey() +" = " + entry.getValue() +";\n");
		}
		return model.toString();
	}	
	

	@Override
	protected void adjustEnvironment(Configuration configuration, int property){
		
		@SuppressWarnings("unchecked")
		List<Object> configurationElements = (List<Object>) configuration.getConfigurationElements();
				
		double x 					= (double)configurationElements.get(0);
		
		
		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			
			double environmentParam 	= Double.parseDouble(entry.getValue().toString());
			
			double stDeviation = 0.3;
			
			double confidenceValue = -1;
			if (x >= 0.75 && x<= 1){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
			}
			else if (x > 0.25 && x < 0.75){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("2");
			}
			else if (x>=0 && x<=0.25){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("3");
			}
			else 
				throw new IllegalArgumentException("Current (x) configuration outside boundaries");
			
			
			if (property==0){// CoveredRooms
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam - confidenceValue * stDeviation));
			}
			else if (property==1){// Utility
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam - confidenceValue * stDeviation));
			}
			else if (property==2){// Cost
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam + confidenceValue * stDeviation));
			}
			else 
				throw new IllegalArgumentException("Property index outside boundaries");
		}
	}

}
