package project5_WordComparison;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Set;
public class main {
	
	public static void main(String[] args) throws FileNotFoundException{
		ArrayList<String> booklist = generateBooklist();  //JaneEyre.txt, War_peace.txt, ...s
		ArrayList<Book> books = new ArrayList<>();
		
		//setting map for each book, null if book cannot be found
		for(int i = 0; i < booklist.size(); i++) {
			if(booklist.get(i)!=null) {
				String s = booklist.get(i);
				s = s.substring(0,s.length()-4);
				Book book = new Book(s);
				
				HashMap<String,Integer> thismap = toMap(booklist.get(i));			//unique words
				if(thismap!= null) {
					book.setUniqueWordsCount(thismap.size());
					book.setMap(thismap);
					book.setProperNounsCount(ProperNouns(booklist.get(i)));
					books.add(book);
				}
			}
		}		
		System.out.println();
		comparebookOrder(books);
		compareWordList(books.get(0));
		System.out.println("____________");
	
	}

	public static void compareWordList(Book bk) throws FileNotFoundException{
		HashMap<String, Integer> mapIn = new HashMap<>();
		HashMap<String, Integer> mapOut = new HashMap<>();
		
		try {
			Scanner scanner = new Scanner(new File("words.txt"));
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] words = line.split(" ");
				
				 
				for(String c: words) {	//word.txt
					if (bk.getMap().containsKey(c)) {mapIn.put(c, 0);}
					else {mapOut.put(c, 0);}
				}
			}
			
			//turn keys into an array for merge sort
			Set<String> keysetsIn = mapIn.keySet();
			Set<String> keysetsOut = mapOut.keySet();
			
			String[] in = new String[mapIn.size()];
			String[] out = new String[mapOut.size()];
			
			int i = 0;int j = 0;
			for(String c: keysetsIn) { in[i++]= c;}
			for(String c: keysetsOut) {out[j++]= c;}
			
			//mergesort
			WordComparator<String> comp = new WordComparator<>();
			MergeSort.mergeSort(in, comp);
			MergeSort.mergeSort(out, comp);
			
			System.out.print("\nWords in focus book " + bk.getName()+ " also in vocabulary list: ");
			for(String s : in) {System.out.print(s + ", ");}
			
			System.out.print("\nWords in vocabulary list not in focus book: " );
			for(String s : out) {System.out.print(s+ ", ");}
			
		}
		catch(FileNotFoundException e){
			e = new FileNotFoundException();
			System.out.print("words.txt is not found");
		}
		
	}


	//__________________________generateBooklist()___________________
	//output: an arraylist of books' filename(string)  - "jane_eere.txt", "xxx.txt",.. //read booklist file
	public static ArrayList<String> generateBooklist()  throws FileNotFoundException{
		File booklistfile = new File("books.txt");
		Scanner scannerBookFileList = null;
		ArrayList<String> booklist = new ArrayList<>();
		try{
			scannerBookFileList = new Scanner(booklistfile);
			while(scannerBookFileList.hasNextLine()) {			//store booklist
				String book = scannerBookFileList.nextLine();				
				booklist.add(book);
			}
		}
		catch(FileNotFoundException e) {e =  new FileNotFoundException("cannot find file");}
		return booklist;   
	}

		
	//input: booklist(arraylist)
	//output:create map for unique word of every book, after mergesort
	public static HashMap<String,Integer> toMap(String bookfilename) throws FileNotFoundException{
			
			ArrayList<String> cleanWords = readBook_CleanWord(bookfilename);	//without punctuation,digits
			HashMap<String,Integer> map = new HashMap<>();
			//don't print invalid books
			if (cleanWords != null) {
					for(String c: cleanWords) {				
						//System.out.println(c);
						if(map.containsKey(c)) 
							map.put(c, map.get(c)+1);
						else 
							map.put(c, 0);
					}
					//System.out.println(" Total unique words in " + bookfilename +" : "+map.size());
					return map;
	//					//print keys for the map
	//					Set<String> keysets = map.keySet();
	//					for(String s: keysets) {
	//						System.out.println(s);
	//					}
		
			}
			else {
				return null;
			}
		
	}	

	
	
	
	public static int ProperNouns(String bookfilename) throws FileNotFoundException{
		File bookfile = new File(bookfilename);
		Scanner scanbook = null;
		int count = 0;
		ArrayList<String> rawAllWords = new ArrayList<>();
		HashMap<String,Integer> map = new HashMap<>();
		
		try {
			scanbook = new Scanner(bookfile);
			while(scanbook.hasNextLine()) {
				String buffline = scanbook.nextLine();
				String[] wordsInLine = buffline.split(" ");		//each line
				for(int i = 0; i < wordsInLine.length; i++) {
					rawAllWords.add(wordsInLine[i]);
				}
			}
			if (rawAllWords == null) {
				return 0;
			}
			
			for(int j = 1; j< rawAllWords.size(); j++) {
				
				if (!rawAllWords.get(j).isEmpty() && !rawAllWords.get(j-1).isEmpty()) {
					String word = clearING_ED(clearPunctuations(rawAllWords.get(j)));
					//remove s in the end
					if (word.endsWith("s") && word.length()>2) {
						word = word.substring(0,word.length()-2);
					}
					//check prev end with .!?":
					String prevword = rawAllWords.get(j-1);					
					char first = word.charAt(0);					
					if(java.lang.Character.isUpperCase(first) && word.length()!= 1) {					
						if(!prevword.endsWith(".") && !prevword.endsWith("\"")  &&  !prevword.endsWith(":") && !prevword.endsWith("!") && !prevword.endsWith("?") && !prevword.endsWith("-") && !prevword.endsWith("'")) {
							if(!map.containsKey(word)) {
								map.put(word,0);
								//System.out.println(word + "   "+ first + " ,ends with: "+ prevword);
								count++;	
							}
						}
					}
				}
			}
			return count;
		}
		catch(FileNotFoundException e) {
			e = new FileNotFoundException();
			System.out.println("!!!!!CANNOT FIND THE BOOK: "+ bookfile);
			return 0;
		}
	}
	
	//_________________read book method__________________________
	//input: bookfile name
	//output: null - if invalid book , arraylist of all the clean words - no ing,ed digit, punctuation
	public static ArrayList<String> readBook_CleanWord(String bookfilename) throws FileNotFoundException {
		File bookfile = new File(bookfilename);
		Scanner scanbook = null;
		ArrayList<String> noPunctuation = new ArrayList<>();			//remove punctuation 
		ArrayList<String> cleanWord = new ArrayList<>();
		
		try {
			scanbook = new Scanner(bookfile);
			
			while(scanbook.hasNextLine()) {
				String buffline = scanbook.nextLine();
				String[] wordsInLine = buffline.split(" ");		//each line
				
				//every word in each line	
				for(int i =0 ; i < wordsInLine.length; i++) {		//wordInLine[i] - will have original ",love" and lots of null
					
					//if string is a space,
					if (!clearPunctuations(wordsInLine[i]).equals(" ")) {	
						
						String noPunc = clearPunctuations(wordsInLine[i]);
						noPunctuation.add(noPunc);	
						cleanWord.add(clearING_ED(noPunc));	
					}			
				}	
			}
			
			return cleanWord;		//no punctuations, no ing, ed
		}
		catch(FileNotFoundException e) {
			System.out.println("!!!!!CANNOT FIND THE BOOK: "+ bookfile);
			return null;
		}
	}
	
	
	
	//_____________________clear Punctuations method _________________________________ 
	//if string is null return " ",
	// if single digit and punctuation - return " "
	// if begin/end punctuations - remove
	public static String clearPunctuations(String s) {
		//break when the first and the last character are both not character/digit
		while(s.length()!= 0) {
			char firstchar =  s.charAt(0);
			char lastchar = s.charAt(s.length()-1); 
			//only 1 char
			//don't remove twice
			if(s.length()==1) {
				if(!Character.isLetter(firstchar)) 			// if not a letter, remove
					return " ";
			}
			// >1 chr
			else {
				if(!Character.isLetter(firstchar)) 		//remove first 
					s = s.substring(1);
				if(!Character.isLetter(lastchar)) 			//remove last
					s = s.substring(0,s.length()-1);
			}
			//break while:
			//if both are removed, break
			if(Character.isLetter(firstchar) && Character.isLetter(lastchar)) 
				{break;}
		}
		
		//if null string
		if(s.isEmpty()) {return " ";}
		//after everything 
		return s;
	}
	
	public static String clearING_ED(String s){
		
		if(s.length()>3) {
			String compareING = s.substring(s.length()-3, s.length());
			String compareED = s.substring(s.length()-2, s.length());
			String compareD = s.substring(s.length()-1);
			
			
			if(compareING.equals("ing")) {
				//System.out.println("!!!!removed ing : " + s + " -> " + s.substring(0,s.length()-3) );
				return s.substring(0,s.length()-3);
			}
			else if(compareED.equals("ed")){
				//System.out.println("!!!!removed ed : " + s + " -> " + s.substring(0,s.length()-3) );
				return s.substring(0,s.length()-2);
			}

			else if(compareD.equals("d") ) {
				
				//System.out.println("!!!!removed d : " + s + " -> " + s.substring(0,s.length()-1) );
				return s.substring(0,s.length()-1);
			}
			
			else {
				//System.out.println( s );
				return s;
			}
		}
		else {
			return s;
		}
	}
	

	//organize the order to compare
	public static void comparebookOrder( ArrayList<Book> bookls) throws FileNotFoundException {
		for(int i = 1; i <bookls.size(); i++) {
			if(bookls.get(i) != null) {
				comparebooks(bookls.get(0), bookls.get(i));
			}
		}
		System.out.println("_______________");
	}
	
	
	//actually compare them - print result
	public static void comparebooks(Book book1, Book book2) throws FileNotFoundException{
		System.out.println("Focus book: " + book1.getName());
		System.out.println( "total word: "+ book1.getUniqueWordsCount());
		System.out.println( "proper nouns: "+ book1.getProperNounsCount());
		System.out.println();
		
		System.out.println("Comparison book:" + book2.getName() );
		System.out.println( "total word: "+ book2.getUniqueWordsCount());
		System.out.println( "proper nouns: "+ book2.getProperNounsCount());
		
		//calculate words in both, and only in each one
		HashMap<String, Integer> mapbk1 = book1.getMap();
		HashMap<String, Integer> mapbk2 = book2.getMap();
		
		HashMap<String, Integer> onlyBk1 = new HashMap<>();
		HashMap<String, Integer> onlyBk2 = new HashMap<>();
		
		int bothc = 0;
		Set<String> keyset1 = mapbk1.keySet();
		for(String s: keyset1) {
			if (mapbk2.containsKey(s)) {bothc++;}
			else {onlyBk1.put(s, 0);}
		}
		Set<String> keyset2 = mapbk2.keySet();
		for(String s : keyset2) {
			if(!mapbk1.containsKey(s)) {onlyBk2.put(s,0);}	
		}
		System.out.println("Words in Both: " + bothc +
				"  Words in " + book1.getName()+" only : " + onlyBk1.size() + 
				"  Words in " + book2.getName()+" only : " + onlyBk2.size() + "\n"   );
		PrintSortedWordList(onlyBk1, book1);
		PrintSortedWordList(onlyBk2, book2);

	}
	
	public static void PrintSortedWordList(HashMap<String,Integer> map, Book bk) {
		String[] arr = new String[map.size()];
		Set<String> keys = map.keySet();
		int i = 0;
		for(String c: keys) {
			arr[i++] = c;
		}
		WordComparator comp = new WordComparator();
		MergeSort.mergeSort(arr,comp);
			
		System.out.print("Words in " + bk.getName() +" : ");
		for(String s: arr) {
			System.out.print(s + ", ");
		}
		System.out.println();
	}
		
	
	
}

	



