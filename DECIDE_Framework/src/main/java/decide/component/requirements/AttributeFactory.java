package decide.component.requirements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.DecideException;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import parser.ast.Property;
import prism.Prism;
import prism.PrismLangException;

public class AttributeFactory {

	private static String propertiesFilename	= Utility.getProperty(DECIDEConstants.PROPERTIES_FILE_KEYWORD);

	public static Map<String, DECIDEAttribute> generate (String internalModel){
		Map<String, DECIDEAttribute> attributesCollection = new ConcurrentHashMap<String, DECIDEAttribute>();
		
		try {
			File propsStrFile 	= new File(propertiesFilename);
			
			Prism prism = new Prism(null);
						
			ModulesFile modelFile = prism.parseModelString(internalModel);// (modelStrFile);
			
			PropertiesFile propsFile = prism.parsePropertiesFile(modelFile, propsStrFile, false); //prism.parsep
			int numProps = propsFile.getNumProperties();
			for (int i=0; i<numProps; i++) {
				Property prop = propsFile.getPropertyObject(i);

//				System.out.println(prop);
				String comment = prop.getComment();
				if (comment != null) {
					String[] commentElements = comment.trim().split ("\\n|\\,");
					
					
					List<String> commentElementsList = new ArrayList<String>(Arrays.asList(commentElements));
					
					//remove those tokens starting with '#', they are simple comments
					Set<String> toBeRemoved = new HashSet<>(); 
					for (String commentElement : commentElementsList) {
						if (commentElement.startsWith("#"))
							toBeRemoved.add(commentElement);
					}
					commentElementsList.removeAll(toBeRemoved);
					
					
					String attrName		= commentElementsList.get(0).trim().toUpperCase();
					String modelName	= commentElementsList.get(1).trim().toLowerCase();
					String attrType		= commentElementsList.get(2).trim().toUpperCase();

					DECIDEAttributeType attributeType = null;
					
					if (attrType.equals(DECIDEAttributeType.LCA.name()))
						attributeType = DECIDEAttributeType.LCA;
					else if (attrType.equals(DECIDEAttributeType.LOCAL_CONTROL.name()))
						attributeType = DECIDEAttributeType.LOCAL_CONTROL;
					else if (attrType.equals(DECIDEAttributeType.BOTH.name()))
						attributeType = DECIDEAttributeType.BOTH;					
					else 
						throw new DecideException("Attribute" + prop + " needs to be associated with the LCA or LOCAL_CONTROL stages. "+ prop.getComment());
					
					
					attributesCollection.put (attrName, new DECIDEAttribute(attrName, modelName, prop.toString(), attributeType));
				}
			}			
		} 
		catch (PrismLangException | IOException | DecideException e) {
			e.printStackTrace();
		}	
		//never happens
		return attributesCollection;
	}
				
	
}
