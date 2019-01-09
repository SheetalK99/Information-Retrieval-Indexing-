package sak170006_hw1;


import java.util.Comparator;
import java.util.HashMap;

// a comparator that compares Strings
public class ValCustomComparator implements Comparator<String>{
 
	HashMap<String, Integer> hashm = new HashMap<String, Integer>();
 
	public ValCustomComparator(HashMap<String, Integer> hashm){
		this.hashm.putAll(hashm);
	}
 
	@Override
	public int compare(String s1, String s2) {
		if(hashm.get(s1) >= hashm.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
}
