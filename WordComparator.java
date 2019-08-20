package project5_WordComparison;

import java.util.Comparator;

public class WordComparator<K> implements Comparator<String>{
	
	@Override
	public int compare(String s1 ,String s2) {
		
		int i = 0;
		int j = 0;
		
		while(i<s1.length() && j <s2.length()) {	
			if ( s1.charAt(i)> s2.charAt(j)) {
				return 1;
			}
			else if(s1.charAt(i)< s2.charAt(j)) {
				return -1;
			}
			else if(s1.charAt(i)== s2.charAt(j)) {
				i++;
				j++;
			}
		}
		
		if(i<s1.length()) {
			return 1;
		}
		else if(j < s2.length()) {
			return -1;
		}
		else {
			return 0;
		}
 
	}	
}
