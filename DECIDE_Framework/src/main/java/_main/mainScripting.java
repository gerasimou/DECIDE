package _main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import caseStudies.uuv.UUVAttributesCollection;
import caseStudies.uuv.UUVConfiguration;


public class mainScripting {

	private mainScripting() {}

    public static void main( String[] args ) throws Exception {
//    	viewAvailableScriptingLanguages();
//    	helloFromJS();
//    	anotherJSexample();
    	robotConfigJS();
//    	invokeMethodExample();
//    	invokeAnimal();
    }
	

    public static void viewAvailableScriptingLanguages() {

        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();

        for (ScriptEngineFactory factory : factories) {

            System.out.println("ScriptEngineFactory Info");

            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();

            System.out.printf("\tScript Engine: %s (%s)%n", engName, engVersion);

            List<String> engNames = factory.getNames();
            for(String name : engNames) {
                System.out.printf("\tEngine Alias: %s%n", name);
            }

            System.out.printf("\tLanguage: %s (%s)%n", langName, langVersion);

        }
    }
    
    
    public static void helloFromJS(){
    	ScriptEngineManager mgr = new ScriptEngineManager();
    	  ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
    	  try {
    	    jsEngine.eval("print('Hello, world!')");
    	    
    	    //Accessing Java Objects From Script
    	    List<String> namesList = new ArrayList<String>();
    	    namesList.add("Jill");
    	    namesList.add("Bob");
    	    namesList.add("Laureen");
    	    namesList.add("Ed");
    	    
    	    jsEngine.put("names", namesList);
    	    System.out.println("Executing in script environment...");
    	    jsEngine.eval("var x;" +
                    "var names = names.toArray();" +
                    "for(x in names) {" +
                    "  print(names[x]);" +
                    "}");//+
//                    "names.add(\"Dana\");");
    	    
    	    jsEngine.put("x", 5.0);
    	    jsEngine.put("y", 2.0);
    	    jsEngine.eval("print(x/y)");
    	    
    	  } catch (ScriptException ex) {
    	      ex.printStackTrace();
    	  } 
    }
    
    
    public static void anotherJSexample(){
    	ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    	String f1		 = "var fun1 = function(name, surname) {" +
    					   "print('Hi there from Javascript, ' + name +','+ surname);"+
    					   "return 'greetings from javascript';}";
    	
    	String f2		=  "var fun2 = function (object) {" +
    					   "print('JS Class Definition: ' + Object.prototype.toString.call(object));}";
    	
    	String f3		= 	"var ArrayList = Java.type('java.util.ArrayList');" +
    						"var list = new ArrayList();" +
    						"list.add('a');" +
    						"list.add('b');" +
    						"list.add('c');" +
    						"var x = list.get(0);" +
    						"print (x+5);";
//    						"for each (var el in list) print(el);  // a, b, c";
    	try {
        	engine.eval(f1);
			engine.eval(f2);
			engine.eval(f3);
			
			Invocable invocableEngine 	= (Invocable) engine;
			Object obj 					= invocableEngine.invokeFunction("fun1", "SPG", "GERAS");
			System.out.println(obj +"\t"+ obj.getClass());

		} catch (ScriptException | NoSuchMethodException e) {
			e.printStackTrace();
		}
    }
    
    
    public static void robotConfigJS(){
    	//create script engine instance
    	ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    	//crate object instance
    	UUVConfiguration robotConfig = new UUVConfiguration(new UUVAttributesCollection(), 7, 3.5);
//    	robotConfig.setResults( new ArrayList<String>(Arrays.asList(new String[]{"11.2", "144"})));
    	//put the object in JS engine
    	engine.put("config", robotConfig);
    	
    	
    	//create a requirement function
    	String function 	=	"var req1Fun = function() {" +
    								"var details = config;"+
//    								 "  for(x in details) {" +
    				                  "    print(details[0][0]);" +
//    				                  "  }" +
    							"}";
    	
    	String printFunc 	=	"var printConfigFunc= function(){"					+
    								"var x = config; print (config.toString());" 	+
    							"}";
    	
    	String printEnergy = "var printEnergyFunc=	 function(){"					+
    								"var energyResult = config.getEnergy() / 8;"	+
    								"print ('Energy:' + config.getEnergy());"		+
    						"return (energyResult);}";
    	
    	try{
    		//evaluate the function
    		engine.eval(printFunc);
    		engine.eval(printEnergy);
    		
			//get the invocable interface
    		Invocable invocableEngine 	= (Invocable) engine;
    		
    		//invoke the function
    		invocableEngine.invokeFunction("printConfigFunc");
    		Object obj = invocableEngine.invokeFunction("printEnergyFunc");
    		System.out.println("Returned from JS: " + obj);

		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }

    
    public static void invokeMethodExample() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        // evaluate JavaScript code that defines an object with one method
        engine.eval("var obj = new Object()");
        engine.eval("obj.hello = function(name) { print('Hello, ' + name) }");

        // expose object defined in the script to the Java application
        Object obj = engine.get("obj");

        // create an Invocable object by casting the script engine object
        Invocable inv = (Invocable) engine;

        // invoke the method named "hello" on the object defined in the script
        // with "Script Method!" as the argument
        inv.invokeMethod(obj, "hello", "Simos!");
    }
    
    
    public static void invokeAnimal(){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        // evaluate JavaScript code that defines an object with one method
        String str =	"var robot = Java.type('robot.RobotConfiguration');" 		+
        				"var  robotObj = new robot(7,2.6);" 						+
        				"print (robotObj.toString());"								+
        				"var configElements = robotObj.getConfigurationElements();" +
        				"for each (el in configElements) print (el);";
        try {
			engine.eval(str);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
