package decide.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import auxiliary.Utility;

import java.util.Map.Entry;

public class ComponentFactory {

	public static List<Component> createComponents(){
		Utility.setup();
		
		//Get the properties set
		Set<Entry<Object,Object>> propertiesSet = Utility.getPropertiesEntrySet();

		List<Component> componentsList = new ArrayList<Component>();		
		
		//Get the iterator
		Iterator<Entry<Object,Object>> iterator = propertiesSet.iterator();
				
		while (iterator.hasNext()){
			Entry<Object, Object> entry = iterator.next();
			String key					= entry.getKey().toString();
			String value				= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
			
			System.out.println(key +"\t"+ value);
			
			Component newComponent = new Component(value);
			componentsList.add(newComponent);
			
//			COMPONENT = 1, SERVER:9991, CLIENT:127.0.0.1:9992, CLIENT:127.0.0.1:9993
		}
		return componentsList;
	}
}
