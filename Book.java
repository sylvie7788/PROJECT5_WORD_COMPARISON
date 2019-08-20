package project5_WordComparison;

import java.util.HashMap;

public class Book {
	
	public Book(String s) {
		
			name = s;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, Integer> getMap() {
		return map;
	}
	public void setMap(HashMap<String, Integer> map) {
		this.map = map;
	}
	public int getUniqueWordsCount() {
		return uniqueWordsCount;
	}
	public void setUniqueWordsCount(int uniqueWordsCount) {
		this.uniqueWordsCount = uniqueWordsCount;
	}
	public int getProperNounsCount() {
		return properNounsCount;
	}
	public void setProperNounsCount(int properNounsCount) {
		this.properNounsCount = properNounsCount;
	}
	private String name;
	private HashMap<String,Integer> map;
	private int uniqueWordsCount;
	private int properNounsCount;
	
	@Override
	public String toString() {
		return name;
	}
	
}
