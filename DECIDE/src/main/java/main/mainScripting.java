package main;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class mainScripting {

	private mainScripting() {}

    public static void main( String[] args ) {
//    	viewAvailableScriptingLanguages();
//    	helloFromJS();
    	anotherJSexample();
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
                    "}"+
                    "names.add(\"Dana\");");
    	    
    	    jsEngine.put("x", 5.0);
    	    jsEngine.put("y", 2.0);
    	    jsEngine.eval("print(x/y)");
    	    
    	  } catch (ScriptException ex) {
    	      ex.printStackTrace();
    	  } 
    }
    
    
    public static void anotherJSexample(){
    	ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    	String f1		 = "var fun1 = function(name) {" +
    					   "print('Hi there from Javascript, ' + name);"+
    					   "return 'greetings from javascript';}";
    	String f2		=  "var fun2 = function (object) {" +
    					   "print('JS Class Definition: ' + Object.prototype.toString.call(object));}";
    	
    	String f3		= 	"var ArrayList = Java.type('java.util.ArrayList');" +
    						"var list = new ArrayList();" +
    						"list.add('a');" +
    						"list.add('b');" +
    						"list.add('c');" +
    						"for each (var el in list) print(el);  // a, b, c";
    	try {
        	engine.eval(f1);
			engine.eval(f2);
			engine.eval(f3);
			
			Invocable invocableEngine 	= (Invocable) engine;
			Object obj 					= invocableEngine.invokeFunction("fun1", "SPG");
			System.out.println(obj +"\t"+ obj.getClass());

		} catch (ScriptException | NoSuchMethodException e) {
			e.printStackTrace();
		}
    }
}
