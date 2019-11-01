package _dummy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortString {

	public static void main (String args[]) {
		//Unsorted list
//		List<String> names = Arrays.asList("Alex", "Charles", "Brian", "David");  
		List<String> names = Arrays.asList("224.224.224.222:8885", "224.224.224.223:8880", "224.224.224.221:8881");  
		 
		//1. Natural order
		names.sort( Comparator.comparing( String::toString ) ); 
		 
		System.out.println(names);
		 
		//2. Reverse order
		names.sort( Comparator.comparing( String::toString ).reversed() );  
		 
		System.out.println(names);
	}
}
