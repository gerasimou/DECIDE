package decide.localAnalysis;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class CapabilitySummary extends LinkedHashMap<String, Map<String,Object>> implements Serializable{

	public CapabilitySummary() {}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String,  Map<String,Object>> entry : this.entrySet()){
			for (Map.Entry<String,  Object> entry2 : entry.getValue().entrySet()){
				str.append("["+ entry2.getKey() +":"+ entry2.getValue() +"]");
			}
		}
		return str.toString();
	}
	
}
