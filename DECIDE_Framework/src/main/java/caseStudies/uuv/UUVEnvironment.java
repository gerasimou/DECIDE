package caseStudies.uuv;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import decide.DECIDE;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class UUVEnvironment extends Environment {

	Random rand = new Random(System.currentTimeMillis());
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVEnvironment.class);
    
    double [][] degradationR1;
    double [][] degradationR2;
    double [][] degradationR3;
    
    /** Time stamp to record mission start activity*/
	protected long missionStartTimeStamp;
	
	public UUVEnvironment() {
		initEnvironment();
		setupSimulationParam();
	}
	
	protected void setupSimulationParam()
	{
		try
		{
		// setup mission time synchronization clock
		missionStartTimeStamp = System.currentTimeMillis();
		
		String[] featuresListR1	= (Utility.getProperty("DEGRADATIONR1")).split(",");
		if(featuresListR1[0].length() <1 || featuresListR1 == null)
			logger.info("No simulation input fount for r1");
		else
		{
			degradationR1 = new double [featuresListR1.length][3];
		for(int i=0 ;i<featuresListR1.length; i++)
		{	
			String [] featureSplit = featuresListR1[i].split(":");
			for(int j=0;j<3;j++)
				degradationR1[i][j] = Double.parseDouble(featureSplit[j].replaceAll("\\s+",""));
			
		}
		}// end else
		
		String[] featuresListR2	= (Utility.getProperty("DEGRADATIONR2")).split(",");
		if(featuresListR2[0].length() <1 || featuresListR2 == null)
			logger.info("No simulation input fount for r2");
		else
		{
			degradationR2 = new double [featuresListR2.length][3];
		for(int i=0 ;i<featuresListR2.length; i++)
		{	
			String [] featureSplit = featuresListR2[i].split(":");
			for(int j=0;j<3;j++)
				degradationR2[i][j] = Double.parseDouble(featureSplit[j].replaceAll("\\s+",""));
			
		}
		}// end else
		
		String[] featuresListR3	= (Utility.getProperty("DEGRADATIONR3")).split(",");
		if(featuresListR3[0].length() <1 || featuresListR3 == null)
			logger.info("No simulation input fount for r3");
		else
		{
			degradationR3 = new double [featuresListR3.length][3];
		for(int i=0 ;i<featuresListR3.length; i++)
		{	
			String [] featureSplit = featuresListR3[i].split(":");
			for(int j=0;j<3;j++)
				degradationR3[i][j] = Double.parseDouble(featureSplit[j].replaceAll("\\s+",""));
			
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
		if(degradationR1 != null)
		{
			for(int i=0;i<degradationR1.length;i++)
			{
				if(degradationR1[i][0]<=currentTime && currentTime<=degradationR1[i][1])
				{	//Double.parseDouble(Utility.getProperty("r1"))
					environmentMap.put("r1", (degradationR1[i][2]/100) *(double)environmentMap.get("r1"));
					logger.info("[Degradation "+degradationR1[i][2]+"% in sensor 1 rate");
					break;
				}
			}
		 environmentMap.put("r1", (double)environmentMap.get("r1") + (rand.nextGaussian()*0.166)+ 0.5);
		}
		if(degradationR2 != null)
		{
			for(int i=0;i<degradationR2.length;i++)
			{
				if(degradationR2[i][0]<=currentTime && currentTime<=degradationR2[i][1])
				{	
					environmentMap.put("r2", (degradationR2[i][2]/100) *(double)environmentMap.get("r2"));
					logger.info("[Degradation "+degradationR2[i][2]+"% in sensor 2 rate");
					break;
				}
			}
		 environmentMap.put("r2", (double)environmentMap.get("r2") + (rand.nextGaussian()*0.166)+ 0.5);
		}
		if(degradationR3 != null)
		{
			for(int i=0;i<degradationR3.length;i++)
			{
				if(degradationR3[i][0]<=currentTime && currentTime<=degradationR3[i][1])
				{	
					environmentMap.put("r3", (degradationR3[i][2]/100) *(double)environmentMap.get("r3"));
					logger.info("[Degradation "+degradationR3[i][2]+"% in sensor 3 rate");
					break;
				}
			}
		 environmentMap.put("r3", (double)environmentMap.get("r3") + (rand.nextGaussian()*0.166)+ 0.5);
		}
		
	}
	
	protected void initEnvironment(){
		environmentMap.put("r1", Double.parseDouble(Utility.getProperty("r1")) + (rand.nextGaussian()*0.166)+ 0.5);
		environmentMap.put("r2", Double.parseDouble(Utility.getProperty("r2")) + (rand.nextGaussian()*0.166)+ 0.5);
		environmentMap.put("r3", Double.parseDouble(Utility.getProperty("r3")) + (rand.nextGaussian()*0.166)+ 0.5);
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
				
		int CSC 					= (int)configurationElements.get(0);
		
		
		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			
			double environmentParam 	= Double.parseDouble(entry.getValue().toString());
			
			double stDeviation = 0.3;
			
			double confidenceValue = -1;
			if (CSC==1 || CSC==2 || CSC==4 || CSC==0){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
			}
			else if (CSC==3 || CSC==5 || CSC==6){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("2");
			}
			else if (CSC==7){
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("3");
			}
			else 
				throw new IllegalArgumentException("Current sensor configuration outside boundaries");
			
			
			if (property==0){
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam - confidenceValue * stDeviation));
			}
			else if (property==1){
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam + confidenceValue * stDeviation));
			}
			else 
				throw new IllegalArgumentException("Property index outside boundaries");
		}
	}

}
