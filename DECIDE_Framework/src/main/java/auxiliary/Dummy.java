package auxiliary;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import decide.StatusRobot;
import decide.configuration.ConfigurationNew;
import decide.qv.prism.PrismAPINew;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;

public class Dummy {


	public static void main(String[] args) {
//		generateProbs2();
//		runPrism();
//		checkConfigurationNew();
//		runPrismAPINew();
		testAtomicReference();
		
	}
	
	
	private static void generateProbs2() {
		final double P3_FULL_MAX = 1;
		final double STEPS		 = 0.4;
		
		double numOfModes = (int)(P3_FULL_MAX/STEPS) + 1;
		System.out.println(numOfModes);
		for (int i=0; i<numOfModes; i++) {
			double v = i*STEPS;
			System.out.println(v);
		}
			
	}
	
	private static void generateProbs() {
		double step = 0.3;
		
		System.out.println((int)(1/step)+1 +"\t"+ 1/step);
		if (step == 0.05)
			return;
		
		for (int full=0; full<=10; full+= step*10) {
			System.out.print(full/10.0 +":\t");
			for (int basic=0; basic<=10-full; basic+=step*10) {
				System.out.print(basic/10. +", ");
			}
			System.out.println();
		}
		
	}

	
	private static void runPrismAPINew() {
		PrismAPINew api = new PrismAPINew("output_PRISM.txt");
		
		String modelString 	= Utility.readFile("models/die/die.pm");		
		String property		= "P=? [ F s=7 & d=5]";		
		double result = api.run(modelString, property);
		System.out.println(result);
	
	}
	
	
	private static void runPrism() {
		try {
			PrismLog mainLog 	= new PrismFileLog("output_PRISM.txt", false); 
			Prism prism 			= new Prism(mainLog, mainLog);
			prism.initialise();
			
			String modelString 				= Utility.readFile("models/die/die.pm");			
//			File propertyFile 				= new File("models/die/die.pctl");
			
			ModulesFile modulesFile 			= prism.parseModelString(modelString);
			prism.buildModel(modulesFile);
			
			PropertiesFile propertiesFile 	= prism.parsePropertiesString(modulesFile, "P=? [ F s=7 & d=5]");//prism.parsePropertiesFile(modulesFile,propertyFile);
			propertiesFile.setUndefinedConstants(null);
			

			Result result = prism.modelCheck (propertiesFile,propertiesFile.getProperty(0));
			System.out.println(result.toString());
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 		
	}

	
	private static void checkConfigurationNew() {
		String configurationFile = "resources" + File.separator + "healthcare" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);
		Utility.setup();
//		ConfigurationNew configNew = new ConfigurationNew();
	}
	

	private static void testAtomicReference() {
		
		AtomicReference<MyMode>  integerMode = new AtomicReference<MyMode>(MyMode.IDLE);
		System.out.println(integerMode.get());		
		integerMode.set(MyMode.STARTUP);
		System.out.println(integerMode.get());
		integerMode.compareAndSet(MyMode.STARTUP, MyMode.OFFLINE);
		System.out.println(integerMode.get());
		integerMode.compareAndSet(MyMode.OFFLINE, MyMode.IDLE);
		System.out.println(integerMode.get());
	}

	
	enum MyMode {
		STABLE_MODE, 
		MAJOR_CHANGE_MODE,
		MINOR_CHANGE_MODE,
		OFFLINE,
		IDLE,
		MAJOR_LOCAL_CHANGE_MODE,
		STARTUP;
	}

}
