import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class DocumentPreprocess {

	public static LinkedHashMap<String, ArrayList<Integer>> proccessDoc(String file) {
		File myDoc = new File(file);

		LinkedHashMap<String, ArrayList<Integer>> terms = new LinkedHashMap<>();

		try {
			Scanner sc = new Scanner(myDoc);
			int pos = 0;
	        while (sc.hasNextLine()) {
	        	String[] l = sc.nextLine().toLowerCase().replaceAll("[.,'\"?:;\\(\\)\\{\\}]", "").split("[\\[\\]\\s]");
	        	for (String word : l) {
	        		pos++;
	        		if (word.trim().length() == 0 || word.compareTo("the") == 0 || word.compareTo("is") == 0 || word.compareTo("are") == 0) {
	      	          // the term was all whitespace, so skip this term
	      	          	pos--;
	        			continue;
	      	        }

	        		if (!terms.containsKey(word)) {
	        	          terms.put(word, new ArrayList<Integer>());
	        		}

	        		terms.get(word).add(pos);

	        	}
	        }
	        sc.close();
	        return terms;
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
