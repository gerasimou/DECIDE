package example.uuv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import auxiliary.Utility;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.requirements.RequirementSet;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.receipt.CLAReceipt;
import network.PeerStatus;
import network.ServerDECIDE;

public class UUVCapabilitySummaryCollection extends CapabilitySummaryCollection {

    
    /** Order of UUV id */
    private int UUVIndex;
    
    /** Order of UUV transmitting ip */
    private String [] UUVIndexArray;
    
    /** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVCapabilitySummaryCollection.class);
    
    /** Map storing a list of optimal configurations **/
	public Map<String, CapabilitySummary[]> optimalCapabilitySummaryMap;
	
	 /** Map storing an ordered list of UUV indexes **/
	public Map<String, Integer> UUVIndexesMap;
	
	/** An iterator for the optimal configurations map**/
	private Iterator<Entry<String, CapabilitySummary[]>> optimalCapabilitySummaryMapIterator;// = configurationsMap.entrySet().iterator();
	
	/** key holding the best configuration for this mode*/
	private String optimalCapabilitySummaryKey;
    
	
	public UUVCapabilitySummaryCollection() {
		//init system characteristics
		optimalCapabilitySummaryMap = new LinkedHashMap<String,  CapabilitySummary[]>();
		UUVIndexesMap				= new HashMap<String, Integer>();
		setupUUVindexes();
	}
	
	
	/**
	 * Add new configuration to this map
	 * @param key
	 * @param value
	 */
	public void insertOptimalConfiguration(String key, CapabilitySummary[] value){
		this.optimalCapabilitySummaryMap.put(key, value);
		
	}
	
	/**
	 * Populate UUV indexes map with id of participating UUVs 
	 * 
	 */
	public void setupUUVindexes(){
		try
		{
		Utility.setup();
		String[] featuresList	= (Utility.getProperty("UUVIndex")).split(",");
		if(featuresList.length <1)
			throw new Exception("UUV indexes not properly formatted");
			
		for(String feature : featuresList)
		{	String [] featureSplit = feature.split(":");
			this.UUVIndexesMap.put(featureSplit[0].replaceAll("\\s+",""), Integer.parseInt(featureSplit[1]));
		}
		}
		catch(ArrayIndexOutOfBoundsException exception)
		{
			logger.error("UUV indexes not properly formatted", exception);
			System.exit(1);
		}
		catch(Exception exception)
		{
			logger.error("UUV indexes not properly formatted", exception);
			System.exit(1);
		}
		
		
	}
	
	public CapabilitySummary[] getOptimalConfiguration() {
		return this.optimalCapabilitySummaryMap.get(optimalCapabilitySummaryKey);
	}
	
	public String getOptimalCapabilitySummaryKey() {
		return optimalCapabilitySummaryKey;
	}

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
	public Iterator<? extends Entry<String, CapabilitySummary[]>> getOptimalConfigurationsMapIterator() {
		//if it is the first time or reached the end of the collection => reset the iterator
		if (optimalCapabilitySummaryMapIterator==null || !optimalCapabilitySummaryMapIterator.hasNext())
			resetOptimalCapabilitySummaryMapIterator();
		return optimalCapabilitySummaryMapIterator;
	}

	
	/**
	 * Reset the iterator of this map
	 */
	public void resetOptimalCapabilitySummaryMapIterator() {
		this.optimalCapabilitySummaryMapIterator = optimalCapabilitySummaryMap.entrySet().iterator();
	}

	@Override
	public boolean findGlobalAllocation(ConfigurationsCollection configurationsCollection, Environment environment)
	{
		int index 	= 0;
		int column 	= 0;
		
		
		/** Clear the optimal configuration map **/
			if(!optimalCapabilitySummaryMap.isEmpty())
				optimalCapabilitySummaryMap.clear();
				
		final int peerSize = concurrentCapabilitySummaryMap.size();
		UUVIndexArray = new String[concurrentCapabilitySummaryMap.size()];
		//configurationsCollection.sizeCapabilitySummaryMap()
		CapabilitySummary[][] globalCLA = new UUVCapabilitySummary [peerSize+1][]; 
		
		
		
		//List<CapabilitySummary[]> list = new ArrayList<>();
		
		// add peer capability Summary results
		
		for (Map.Entry<String, CapabilitySummary[]> mapEntry : concurrentCapabilitySummaryMap.entrySet())
		{
			UUVIndexArray[index] = mapEntry.getKey();
			CapabilitySummary[] capabilitySummaryArray = mapEntry.getValue();
			globalCLA[index] = new UUVCapabilitySummary[capabilitySummaryArray.length]; 
			for(CapabilitySummary cs: capabilitySummaryArray)
				globalCLA[index][column++] = cs;
			index++;
			column=0;
		}
		
		// add local capability Summary results
		CapabilitySummary cs;
		globalCLA[index] = new UUVCapabilitySummary[configurationsCollection.sizeCapabilitySummaryMap()];
		while ((cs = configurationsCollection.getNextCapabilitySummary()) != null)
			globalCLA[index][column++] = cs;
			
			
			boolean result = false;
			this.UUVIndex = index;
			//peerConfigs = list.toArray(new Configuration [list.size()][]);
			result = findBestCapabilitySummaryforRequirements((UUVCapabilitySummary[][])globalCLA,index, environment);
			
			if (result)
				return true;
			
			return false;	
	}
	
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
	public boolean findOptimalEnergyConsumption(){
		
		double bestTotal	= Double.MAX_VALUE;
		double total		= 0;
		boolean success 	= false;
		
		for (Map.Entry<String, CapabilitySummary[]> entry : optimalCapabilitySummaryMap.entrySet()){
			CapabilitySummary[] csArray = entry.getValue();
				//for(CapabilitySummary capabilitySummary: csArray)
				//{
					total = ((UUVCapabilitySummary)csArray[csArray.length-1]).getBound();
					
			if( total < bestTotal)
			{
				this.optimalCapabilitySummaryKey = entry.getKey();
				bestTotal = total;
				success = true;
			}
				//}
				
			
		}
		logEvents("Active["+ this.optimalCapabilitySummaryKey+"]");
		
		return success;
		
		/*
		System.out.println("The Configuration that maximize contribution has the path"+ this.bestOptimalConfigurationKey+
				"and configuration values are ["+optimalConfigurationsMap.get(this.bestOptimalConfigurationKey).toString()+"]");
	*/
	}
	/**
	 * Produce a List<CapabilitySummary[]> which contains every combination which can be
	 * made by taking one Capability Summary from each inner Capability Summary array within the
	 * provided two-dimensional Capability Summary array.
	 * @param twoDimCapabilitySummary Array a two-dimensional Capability Summary array which contains
	 * capabilities arrays of variable length.
	 * @param reduceSpace eliminate the i-th rows from the given two-dimensional array to reduce
	 *  probability space.
	 * @return a List which contains every Capability Summary which can be formed by taking
	 * one Configuration from each Configuration array within the specified two-dimensional
	 * array.
	 */
	public List<UUVCapabilitySummary[]> combinations(UUVCapabilitySummary[][] twoDimConfigurationArray, int reduceSpace) {
	    // keep track of the size of each inner Configuration array
	    int sizeArray[] = new int[twoDimConfigurationArray.length - reduceSpace];

	    // keep track of the index of each inner Configuration array which will be used
	    // to make the next combination
	    int counterArray[] = new int[twoDimConfigurationArray.length - reduceSpace];

	    // Discover the size of each inner array and populate sizeArray.
	    // Also calculate the total number of combinations possible using the
	    // inner Configuration array sizes.
	    int totalCombinationCount = 1;
	    for(int i = 0; i < (twoDimConfigurationArray.length - reduceSpace); ++i) {
	        sizeArray[i] = twoDimConfigurationArray[i].length;
	        totalCombinationCount *= twoDimConfigurationArray[i].length;
	    }

	    // Store the combinations in a List of Configuration array
	    List<UUVCapabilitySummary[]> combinationList = new ArrayList<UUVCapabilitySummary[]>(totalCombinationCount);

	    UUVCapabilitySummary[] configs;  // array of configs to store the configuration combinations
	    // Configuration array index
	    int indexKey = 0;
	    for (int countdown = totalCombinationCount; countdown > 0; --countdown) {
	        // Run through the inner arrays, grabbing the member from the index
	        // specified by the counterArray for each inner array, and build a
	        // combination of configurations.
	    		// Reminder to change the size to something more dynamic
	    		configs = new UUVCapabilitySummary [twoDimConfigurationArray.length - reduceSpace];
	        for(int i = 0; i < (twoDimConfigurationArray.length - reduceSpace); ++i) {
	            configs[indexKey] = twoDimConfigurationArray[i][counterArray[i]];
	            indexKey++;
	        }
	        combinationList.add(configs);  // add new combination to list
	        indexKey = 0;				  // reset index array pointer

	        // Now we need to increment the counterArray so that the next
	        // combination is taken on the next iteration of this loop.
	        for(int incIndex = (twoDimConfigurationArray.length - 1 - reduceSpace); incIndex >= 0; --incIndex) {
	            if(counterArray[incIndex] + 1 < sizeArray[incIndex]) {
	                ++counterArray[incIndex];
	                // None of the indices of higher significance need to be
	                // incremented, so jump out of this for loop at this point.
	                break;
	            }
	            // The index at this position is at its max value, so zero it
	            // and continue this loop to increment the index which is more
	            // significant than this one.
	            counterArray[incIndex] = 0;
	        }
	    }
	    return combinationList;
	}
	
	/**
	 * Identify the best configuration for combination of configurations
	 */
	public boolean findBestCapabilitySummaryforRequirements(UUVCapabilitySummary[][] capabilitySummaries, int id, Environment environment)
	{
		int key = 0;
		boolean reqsSatified = false;
//		List<CapabilitySummary> peerCapabilitySummaries = new ArrayList<>();
		String configurationPath = "";
		
		// if only one UUV active
		if(capabilitySummaries.length<=1)
		{
			//CapabilitySummary capabilitySummary;
			for (int i=0; i< capabilitySummaries[id].length;i++){
				
				
				//1) determine if the config satisfies global requirements
				capabilitySummaries[id][i].evaluateGlobalRequirements(environment);
			
				reqsSatified = capabilitySummaries[id][i].requirementsSatisfied(RequirementSet.GLOBAL);
							
					if(reqsSatified)
					{
						insertOptimalConfiguration(String.valueOf(id)+"_"+String.valueOf(capabilitySummaries[id][i].getCSC())
						, new UUVCapabilitySummary[]{capabilitySummaries[id][i]}); 
						reqsSatified = false;
					}
					
						
			}
			
		}
		
		// Check global requirements for participating peer
		else {
//		for(int i=0; i<capabilitySummaries.length;i++)
//		{
		
		List<UUVCapabilitySummary[]> combinations = combinations(capabilitySummaries,0);
		
		
		
			for (UUVCapabilitySummary [] capabilitySummaryArray : combinations) {
			
			//1) Clone UUVCapabilitySummary object to store the requirement results
				capabilitySummaryArray[capabilitySummaryArray.length-1] = new UUVCapabilitySummary(capabilitySummaryArray[capabilitySummaryArray.length-1]);
			
			//2) determine if the config satisfies global requirements
				capabilitySummaryArray[capabilitySummaryArray.length-1].evaluateGlobalRequirements(environment, capabilitySummaryArray );
			
				reqsSatified = capabilitySummaryArray[capabilitySummaryArray.length-1].requirementsSatisfied(RequirementSet.GLOBAL);
			
				if(reqsSatified)
					{
					int j = 0;
					configurationPath = "[";
					for(UUVCapabilitySummary uuvCS: capabilitySummaryArray )
					{
						if(j < capabilitySummaryArray.length - 1)
							configurationPath += "ID:"+String.valueOf(UUVIndexesMap.get(UUVIndexArray[j++]))+uuvCS.toString()+",";
						else
							configurationPath += "ID:"+Knowledge.getID()+uuvCS.toString()+"]";
					}
					optimalCapabilitySummaryMap.put(configurationPath, capabilitySummaryArray);
					
					reqsSatified = false;
					}
			}
			
	//	}// end for(int i=0; i<capabilitySummaries.length;i++)
		}// end else
		//Find optimal configuration that maximize system contribution
		reqsSatified = findOptimalEnergyConsumption();
		if(reqsSatified)
			return true;
		
		return false;
		
	}
	
}

