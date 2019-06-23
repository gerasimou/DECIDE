package caseStudies.activityBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import caseStudies.uuv.UUVConfiguration;
import decide.component.requirements.RequirementSet;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.receipt.CLAReceipt;
import network.PeerStatus;
import network.ReceiverDECIDE;

public class ActivityBotConfigurationsCollection extends ConfigurationsCollection {

    /** System characteristics*/
    private int NUM_OF_CONFIGS	;//possible robot configurations
    
    /** Logging system events*/
    final static Logger logger = Logger.getLogger(ActivityBotConfigurationsCollection.class);
    
    /** Map storing the optimal configuration all mode**/
	//public Map<String, OptimalConfiguration> optimalConfigurationsMap;
	
	/** An iterator for the configurations map of this mode**/
	//private Iterator<Entry<String, OptimalConfiguration>> optimalConfigurationsMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	//private String bestOptimalConfigurationKey;
    
	public ActivityBotConfigurationsCollection() {
		//init system characteristics
	    
	    this.NUM_OF_CONFIGS	= 3; 	//possible robot configurations (value of (s))
	    this.numOfModes 		= 5;		//possible robot configurations (value of (x))  
	    initModes();
	    //optimalConfigurationsMap = new LinkedHashMap<String, OptimalConfiguration>();

	    
	}
	@Override
	protected void initModes() {
		Utility.setup();
		String [] features;
		String value;
		try {
		
		
		
		double[] modex = new double[]{0.0001,0.25,0.5,0.75,0.9998};
		
		
		
		// Set up Robot variable configuration speeds
		value = Utility.getProperty("ROBOT_SPEED");
		value = value.replaceAll("\\s+","");
		features = value.split(",");
		this.NUM_OF_CONFIGS = features.length;
		int[] speed = new int[this.NUM_OF_CONFIGS];
		
		for (int i=0; i<features.length; i++)
		speed[i] = Integer.parseInt(features[i]);
		
		// Set up Robot variable speed costs
		value = Utility.getProperty("SPEED_COST");
		value = value.replaceAll("\\s+","");
		features = value.split(",");
		double[] speedCost = new double[this.NUM_OF_CONFIGS];
				
		for (int i=0; i<features.length; i++)
			speedCost[i] = Double.parseDouble(features[i]);	
		
		// Set up Robot variable task rates
		value = Utility.getProperty("TASK_RATE");
		value = value.replaceAll("\\s+","");
		features = value.split(",");
		double[] rates = new double [3];
						
		for (int i=0; i<features.length; i++)
			rates[i] = Double.parseDouble(features[i]);	
		
		for (int i=0; i<numOfModes; i++){
			ActivityBotMode mode = new ActivityBotMode();
			
			for (int j=0; i<NUM_OF_CONFIGS; j++)
			mode.insertConfiguration(Double.toString(modex[i]), new ActivityBotConfiguration(modex[i], speed[j],speedCost[j],rates[0],rates[1],rates[2]));
			
			
			modesCollection.add(mode);
		}
		}
		catch (IllegalArgumentException ex) {
			logger.error(ex);
			System.exit(-1);
			
		}
		catch (Exception e)
		{
			logger.error(e);
			System.exit(-1);
			
		}
//		int[] cost2 = new int[]{2,7,8};
//		int[] contrib2 = new int[] {11,21,24};
//		for (int i=1; i<=numOfModes; i++){
//			Mode mode = new Mode();
//			//Random rand = new Random();
//			//int  cost = rand.nextInt(10) + 1;
//			//int  contrib = (int) (1+12*Math.log(cost));
////			mode.insertConfiguration(Integer.toString(i), new ActivityBotConfiguration(i, contrib, cost));
//			mode.insertConfiguration(Integer.toString(i), new ActivityBotConfiguration(i, contrib2[i-1], cost2[i-1]));
//			
//			
//			modesCollection.add(mode);
//		}
	}
	/**
	 * Identify and select the optimal configuration based on cost
	 */
	@Override
	public boolean findOptimalLocalConfiguration(){
		
		double bestTotal	= Double.MAX_VALUE;
		double total		= 0;
		boolean success 	= false;
		String best 		= null;
		
		for (Map.Entry<String, Configuration> entry : optimalConfigurationMap.entrySet()){
			ActivityBotConfiguration activityBotConfiguration = (ActivityBotConfiguration)entry.getValue();
				//for(CapabilitySummary capabilitySummary: csArray)
				//{
					total = (double)activityBotConfiguration.getCost();
					
			if( total > bestTotal) {
				best		   = entry.getKey();
				bestTotal = total;
				success = true;
			}	
		}
		logger.info("Active Configuration["+ getOptimalConfiguration()+"]");

		setOptimalConfigurationKey(best);
		return success;
		
		/*
		System.out.println("The Configuration that maximize contribution has the path"+ this.bestOptimalConfigurationKey+
				"and configuration values are ["+optimalConfigurationsMap.get(this.bestOptimalConfigurationKey).toString()+"]");
	*/
	}
	
	/**
	 * Add new configuration to this map
	 * @param key
	 * @param value
	 */
//	public void insertOptimalConfiguration(String key, OptimalConfiguration value){
//		this.optimalConfigurationsMap.put(key, value);
//		
//	}
	
//	@Override
//	public OptimalConfiguration getOptimalConfiguration() {
//		return this.optimalConfigurationsMap.get(bestOptimalConfigurationKey);
//	}
//	
//	public String getBestConfigurationKey() {
//		return bestOptimalConfigurationKey;
//	}

	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug(parameter);
		else
			logger.info(parameter);

		//logger.error("This is error : " + parameter);
	}


	/**
	 * Retrieve the iterator for this map
	 * @return
	 */
//	public Iterator<? extends Entry<String, OptimalConfiguration>> getOptimalConfigurationsMapIterator() {
//		//if it is the first time or reached the end of the collection => reset the iterator
//		if (optimalConfigurationsMapIterator==null || !optimalConfigurationsMapIterator.hasNext())
//			resetOptimalConfigurationsMapIterator();
//		return optimalConfigurationsMapIterator;
//	}

	
	/**
	 * Reset the iterator of this map
	 */
//	public void resetOptimalConfigurationsMapIterator() {
//		this.optimalConfigurationsMapIterator = optimalConfigurationsMap.entrySet().iterator();
//	}

	//@Override
//	public boolean findBestforSystemReqyirements(CLAReceipt cla)
//	{
//		Configuration config = null;
//		/** Clear the optimal configuration map **/
//			if(!optimalConfigurationsMap.isEmpty())
//				optimalConfigurationsMap.clear();
//				
//		final int serverSize = cla.getServersList().size();
//		Configuration[][] peerConfigs; 
//		int peerId = 0;
//		
//		
//		List<Configuration[]> list = new ArrayList<>();
//		
//		for (ServerDECIDE server : cla.getServersList()){
//			if (server.getAtomicPeerStatus().get()== PeerStatus.ALIVE) {
//				
//				
//					
//				peerId = server.getID();
//				Configuration[] configs = new Configuration [server.getCapabilitySummary().concurrentConfigurationsMap.size()];
//				//Configuration[] configs = (Configuration[]) (server.getCapabilitySummary().concurrentConfigurationsMap.values().toArray(configs));
//				configs = server.getCapabilitySummary().concurrentConfigurationsMap.values().toArray(configs);
//				
//				// you can remove this part if each peer has stored its corresponding id[keep for now]
//				for( Configuration peerConfig : configs)
//				{
//					((ActivityBotConfiguration)peerConfig).setId(peerId);
//				
//				
//				}
//				
//				list.add(configs);
//				
//			}
//			
//		}
//		//if(list.size() > 0) {
//			
//			List <Configuration> localList = new ArrayList<>();
//			while ((config = getNextConfiguration()) != null){
//				// should be done in configuration initiation NOT here!
//				((ActivityBotConfiguration)config).setId(cla.getiD());
//				localList.add(config);
//			}
//			Configuration[] localConfigArray = new Configuration [localList.size()]; 
//			localConfigArray = localList.toArray(localConfigArray);
//			list.add(localConfigArray);
//			
//			// arrange list elements bases on Configuration ID
//			if(list.size()>1) {
//			list = sortAscending(list);
//			// try printing list content for assurance
//			if(logger.isDebugEnabled())
//			{
//				logger.debug("[CheckingOrder]");
//				for(Configuration [] configurationArray:list)
//				{
//			 	logger.debug(((ActivityBotConfiguration)configurationArray[0]).getId());
//				}
//				logger.debug("[EndCheckingOrder]");
//			}
//			}
//			
//			
//			boolean result = false;
//			peerConfigs = list.toArray(new Configuration [list.size()][]);
//			result = findBestConfigurationforRequirements(peerConfigs,cla.getiD());
//			
//			if (result)
//				return true;
//	
//			/*
//			Configuration [] configArray2;
//			for(int i=list.size()-1;i>= 0;i--)
//			{
//				peerConfigs = list.toArray(new Configuration [list.size()][]);
//				result = findBestConfigurationforRequirements(peerConfigs,cla.getiD());
//				
//				if (result)
//					return optimalConfigurationsMap.get(this.bestOptimalConfigurationKey);
//				
//				configArray2 = list.get(i);
//				if(((ActivityBotConfiguration)configArray2[0]).getId() == cla.getiD())
//					return null;
//				else
//					list.remove(i);
//					
//				
//			}
//			*/
//			
//			return false;
//		
//	}
//	
	// make the sort descending
	/**
	 * Sort configuration list based on component id
	 */
//	public List<Configuration[]>  sortAscending(List<Configuration[]> list) {         
//	      
//		Collections.sort(list, idComparator);
//	    return list;     
//	  }  
	
//	 public static Comparator<Configuration[]> idComparator = new Comparator<Configuration[]>() {         
//		    @Override         
//		    public int compare(Configuration[] jc1, Configuration[] jc2) {             
//		      return (((ActivityBotConfiguration)jc2[0]).getId() < ((ActivityBotConfiguration)jc1[0]).getId() ? 1 :                     
//		              (((ActivityBotConfiguration)jc2[0]).getId() == ((ActivityBotConfiguration)jc1[0]).getId() ? 0 : -1));           
//		    }     
//		  }; 
	
	/**
	 * Identify and select the optimal configuration based on cost
	 */
//	public boolean findContributionOptimalConfiguration(){
//		
//		double bestTotal = 100000;
//		boolean success = false;
//		
//		for (Map.Entry<String, OptimalConfiguration> entry : optimalConfigurationsMap.entrySet()){
//			OptimalConfiguration optConfig = entry.getValue();
//						
//			if(optConfig.getCostTotal() < bestTotal)
//			{
//				this.bestOptimalConfigurationKey = entry.getKey();
//				bestTotal = optConfig.getCostTotal();
//				success = true;
//			}
//				
//			
//		}
//		logEvents("Active["+ this.bestOptimalConfigurationKey+"] "+success);
//		
//		return success;
//		
//		/*
//		System.out.println("The Configuration that maximize contribution has the path"+ this.bestOptimalConfigurationKey+
//				"and configuration values are ["+optimalConfigurationsMap.get(this.bestOptimalConfigurationKey).toString()+"]");
//	*/
//	}
	/**
	 * Produce a List<Configuration[]> which contains every combination which can be
	 * made by taking one Configuration from each inner Configuration array within the
	 * provided two-dimensional Configuration array.
	 * @param twoDimConfigurationArray a two-dimensional Configuration array which contains
	 * Configuration arrays of variable length.
	 * @param reduceSpace eliminate the i-th rows from the given two-dimensional array to reduce
	 *  probability space.
	 * @return a List which contains every Configuration which can be formed by taking
	 * one Configuration from each Configuration array within the specified two-dimensional
	 * array.
	 */
//	public List<Configuration[]> combinations(Configuration[][] twoDimConfigurationArray, int reduceSpace) {
//	    // keep track of the size of each inner Configuration array
//	    int sizeArray[] = new int[twoDimConfigurationArray.length - reduceSpace];
//
//	    // keep track of the index of each inner Configuration array which will be used
//	    // to make the next combination
//	    int counterArray[] = new int[twoDimConfigurationArray.length - reduceSpace];
//
//	    // Discover the size of each inner array and populate sizeArray.
//	    // Also calculate the total number of combinations possible using the
//	    // inner Configuration array sizes.
//	    int totalCombinationCount = 1;
//	    for(int i = 0; i < (twoDimConfigurationArray.length - reduceSpace); ++i) {
//	        sizeArray[i] = twoDimConfigurationArray[i].length;
//	        totalCombinationCount *= twoDimConfigurationArray[i].length;
//	    }
//
//	    // Store the combinations in a List of Configuration array
//	    List<Configuration[]> combinationList = new ArrayList<Configuration[]>(totalCombinationCount);
//
//	    Configuration[] configs;  // array of configs to store the configuration combinations
//	    // Configuration array index
//	    int indexKey = 0;
//	    for (int countdown = totalCombinationCount; countdown > 0; --countdown) {
//	        // Run through the inner arrays, grabbing the member from the index
//	        // specified by the counterArray for each inner array, and build a
//	        // combination of configurations.
//	    		// Reminder to change the size to something more dynamic
//	    		configs = new Configuration [twoDimConfigurationArray.length - reduceSpace];
//	        for(int i = 0; i < (twoDimConfigurationArray.length - reduceSpace); ++i) {
//	            configs[indexKey] = twoDimConfigurationArray[i][counterArray[i]];
//	            indexKey++;
//	        }
//	        combinationList.add(configs);  // add new combination to list
//	        indexKey = 0;				  // reset index array pointer
//
//	        // Now we need to increment the counterArray so that the next
//	        // combination is taken on the next iteration of this loop.
//	        for(int incIndex = (twoDimConfigurationArray.length - 1 - reduceSpace); incIndex >= 0; --incIndex) {
//	            if(counterArray[incIndex] + 1 < sizeArray[incIndex]) {
//	                ++counterArray[incIndex];
//	                // None of the indices of higher significance need to be
//	                // incremented, so jump out of this for loop at this point.
//	                break;
//	            }
//	            // The index at this position is at its max value, so zero it
//	            // and continue this loop to increment the index which is more
//	            // significant than this one.
//	            counterArray[incIndex] = 0;
//	        }
//	    }
//	    return combinationList;
//	}
//	
	/**
	 * Identify the best configuration for combination of configurations
	 */
//	public boolean findBestConfigurationforRequirements(Configuration[][] receivedConfigs, int id)
//	{
//		
//		Environment environment = null;
//		OptimalConfiguration optiConfig = null;
//		int key = 0;
//		boolean reqsSatified = false;
//		List<Configuration> peerConfigs = new ArrayList<>();
//		String configurationPath = "";
//		
//		if(receivedConfigs.length<=1)
//		{
//			Configuration config;
//			while ((config = getNextConfiguration()) != null){
//				
//				
//				//1) determine if the config satisfies global requirements
//				config.evaluateGlobalRequirements(environment);
//				//config.evaluateLocalRequirements(environment);
//							
//				//2) check whether the config satisfies local requirements
//				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
//							
//				//3) if true, get the bound (attribute analysis 2)
//				//double bound = reqsSatified ? config.getBound() : 0.0;
//				reqsSatified = config.requirementsSatisfied(RequirementSet.GLOBAL);
//							
//					if(reqsSatified)
//					{
//						optiConfig = new OptimalConfiguration(config, (double) config.getGlobalRequirementsResults().get("R1"), (double) config.getGlobalRequirementsResults().get("R2"));
//						optimalConfigurationsMap.put(config.toString(), optiConfig);
//						reqsSatified = false;
//					}
//					
//						
//			}
//			//Find optimal configuration that maximize system contribution
//			reqsSatified = findContributionOptimalConfiguration();
//			if(reqsSatified)
//				return true;
//			
//			return false;
//		}
//		
//		 
//		Configuration config = new ActivityBotConfiguration(0,0,0,0,0,0);
//		
//		for(int i=0; i<receivedConfigs.length;i++)
//		{
//		
//		List<Configuration[]> combinations = combinations(receivedConfigs,i);
//		
//		
//		
//			for (Configuration [] configurationArray : combinations) {
//				
//			
//			//1) determine if the config satisfies global requirements
//			config.evaluateGlobalRequirementsforCombinations(environment, configurationArray );
//			
//			reqsSatified = config.requirementsSatisfied(RequirementSet.GLOBAL);
//			
//			if(reqsSatified)
//			{
//				
//				
//				peerConfigs.addAll(Arrays.asList(configurationArray));
//				
//				for(Configuration configuration : peerConfigs)
//				{
//					if(((ActivityBotConfiguration)configuration).getId() == id)
//					{
//						for (Map.Entry<String, Object> entry : config.getGlobalRequirementsResults().entrySet()){
//							configuration.getGlobalRequirementsResults().put(entry.getKey(), entry.getValue());
//						}
//						
//						config = configuration;
//						
//					}
//					// Place the branch path of configuration as the key
//					configurationPath+= "<"+configuration.toString()+">";
//					
//
//				}
//				
//				optiConfig = new OptimalConfiguration(config,new ArrayList<>(Arrays.asList(configurationArray)) , (double) config.getGlobalRequirementsResults().get("R1"), (double) config.getGlobalRequirementsResults().get("R2"));
//
//				optimalConfigurationsMap.put("<"+config.toString()+">["+configurationPath+"]", optiConfig);
//				
//				
//				// clearup reusable elements
//				reqsSatified = false;
//				configurationPath = "";
//				peerConfigs.clear();
//				
//				
//			}
//			
//			}// end for (Configuration [] configs : combinations)
//			
//			//Find optimal configuration that maximize system contribution
//			reqsSatified = findContributionOptimalConfiguration();
//			if(reqsSatified)
//				return true;
//			else
//			{
//				Configuration [] configurationArray = combinations.get(combinations.size()-1);
//				
//				if(((ActivityBotConfiguration)configurationArray[configurationArray.length-1]).getId() == id)
//					return false;
//			}
//		}
//		return false;
//		
//	}
//	
	/**
	 * Identify the best configuration for this mode
	 * please remember to remove this function, not used anymore 
	 */
//	public void findBestConfigurationforRequirements(Configuration config, Configuration[][] receivedConfigs, int index){
//		
//		Environment environment = null;
//		OptimalConfiguration optiConfig = null;
//		int key = 0;
//		boolean reqsSatified = false;
//		
//		
//		
//		//evaluate the configuration for this mode
//		if(receivedConfigs != null)
//		{
//		
//		switch (receivedConfigs.length)
//		{
//		
//		// Only one peer is active
//		case 1:
//			
//			
//			for(int i=0; i<receivedConfigs[0].length;i++)
//			{
//				//1) determine if the config satisfies global requirements
//				config.evaluateGlobalRequirements(environment, receivedConfigs[0][i] );
//				
//				reqsSatified = config.requirementsSatisfied(RequirementSet.GLOBAL);
//				
//				if(reqsSatified)
//				{
//					
//					optiConfig = new OptimalConfiguration(config, receivedConfigs[0][i], (double) config.getGlobalRequirementsResults().get("R1"), (double) config.getGlobalRequirementsResults().get("R2"));
//					optimalConfigurationsMap.put("<"+config.toString()+"><"+receivedConfigs[0][i].toString()+">", optiConfig);
//					// reset boolean flag
//					reqsSatified = false;
//				}
//				
//			}
//	
//			break;
//		
//		// There are many peers, construct tree of configurations
//		default:
//			
//			
//
//			break;
//			}// end switch
//		}// end if(peerConfigs != null)
//		else
//		{
//			// if -1, then no peers are listening (Alone mode)
//					
//			
//			//Configuration peerConfig = new ActivityBotConfiguration(1, 0, 0);
//			//1) determine if the config satisfies global requirements
//			config.evaluateGlobalRequirements(environment);
//			//config.evaluateLocalRequirements(environment);
//						
//			//2) check whether the config satisfies local requirements
//			//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
//						
//			//3) if true, get the bound (attribute analysis 2)
//			//double bound = reqsSatified ? config.getBound() : 0.0;
//			reqsSatified = config.requirementsSatisfied(RequirementSet.GLOBAL);
//						
//				if(reqsSatified)
//				{
//					optiConfig = new OptimalConfiguration(config, (double) config.getGlobalRequirementsResults().get("R1"), (double) config.getGlobalRequirementsResults().get("R2"));
//					optimalConfigurationsMap.put(config.toString(), optiConfig);
//				}
//						
//						
//		}// end else
//
//	}

	
	
	
	
}

