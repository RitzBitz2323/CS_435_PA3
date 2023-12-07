
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionalIndex {

	File dataFolder;
	HashMap<String, HashMap<String, ArrayList<Integer>>> invertedIndex;
	
	PositionalIndex(String folder) {
		dataFolder = new File(folder);
		
		invertedIndex = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
		
		for (String doc : dataFolder.list()) {
			LinkedHashMap<String, ArrayList<Integer>> docProcessed = DocumentPreprocess.proccessDoc(dataFolder.getAbsolutePath() + "\\" + doc);
			for (String term : docProcessed.keySet()) {
				if (!invertedIndex.containsKey(term)) {
      	          invertedIndex.put(term, new HashMap<String, ArrayList<Integer>>());
				}

				invertedIndex.get(term).put(doc, docProcessed.get(term));
			}
		}

	}
	
  public int termFrequency(String term, String Doc){
        Map<String, List<Integer>> enumerationsOfTerm = PositionalIndex.get(term);
        if(PositionalIndex.containsKey(term) == false){
            return 0;
        }
        else if(enumerationsOfTerm.containsKey(Doc) == false){
            return 0;
        }
        return enumerationsOfTerm.get(Doc).size();
    }

    public int docFrequency(String term) {
        if (PositionalIndex.containsKey(term) == false) {
          return 0;
        }
        else{
        return PositionalIndex.get(term).size();
        }
    }
// 	int termFrequency(String term, String Doc) {
// 		return invertedIndex.get(term.toLowerCase()).get(Doc).size();
// 	}
	
// 	int docFrequency(String term) {
// 		return invertedIndex.get(term.toLowerCase()).keySet().size();
// 	}
	
	String postingsList(String t) {
		//TODO
		HashMap<String, ArrayList<Integer>> postings = invertedIndex.get(t.toLowerCase());
		String postList = "[";
		//loops...
		postings.forEach((key, value) -> {
				String docList = "<" + key + " : ";
				ArrayList<Integer> vals = value;
				for (int i = 0; i < vals.size(); i++) {
					docList += vals.get(i);
					if (i != vals.size() - 1) {
						docList += ",";
					}
				}
				docList += ">";
				postList += docList + ",";
//				System.out.print(docList);
		});
		postList += "]";
//		System.out.println(postList);
		return postList;
	}
	
	double weight(String t, String d) {
		// w(t,d) = sqrt(TFtd) * log10(N/DFt)
		return Math.sqrt(termFrequency(t, d)) * Math.log10( ((double) dataFolder.list().length / docFrequency(t)));
	}
	
	double TPScore(String query, String doc) {
		//TODO
		return 0;
	}
	
	double VSScore(String query, String doc) {
		//TODO
		return 0;
	}
	
	double Relevance(String query, String doc) {
		return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc); 
	}
}