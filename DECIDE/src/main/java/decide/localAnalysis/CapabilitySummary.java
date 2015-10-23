package decide.localAnalysis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class CapabilitySummary extends LinkedHashMap<String, Object[]> implements Serializable{

	public CapabilitySummary() {}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, Object[]> entry : this.entrySet()){
			str.append("["+ entry.getKey() +":"+ Arrays.toString(entry.getValue()) +"]");
		}
		return str.toString();
	}
}
