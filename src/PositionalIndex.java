
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
			LinkedHashMap<String, ArrayList<Integer>> docProcessed = DocumentPreprocess.proccessDoc(dataFolder.getAbsolutePath() + '/' + doc);
            // "\\" could be inputted in process doc 
			for (String term : docProcessed.keySet()) {
				if (!invertedIndex.containsKey(term)) {
      	          invertedIndex.put(term, new HashMap<String, ArrayList<Integer>>());
				}

				invertedIndex.get(term).put(doc, docProcessed.get(term));
			}
		}

	}
	
  public int termFrequency(String term, String Doc){
        HashMap<String, ArrayList<Integer>> enumerationsOfTerm = invertedIndex.get(term);
        if(invertedIndex.containsKey(term) == false){
            return 0;
        }
        else if(enumerationsOfTerm.containsKey(Doc) == false){
            return 0;
        }
        return enumerationsOfTerm.get(Doc).size();
    }

    public int docFrequency(String term) {
        if (invertedIndex.containsKey(term) == false) {
          return 0;
        }
        else{
        return invertedIndex.get(term).size();
        }
    }
// 	int termFrequency(String term, String Doc) {
// 		return invertedIndex.get(term.toLowerCase()).get(Doc).size();
// 	}
	
// 	int docFrequency(String term) {
// 		return invertedIndex.get(term.toLowerCase()).keySet().size();
// 	}
	
// 	String postingsList(String t) {
// 		//TODO
// 		HashMap<String, ArrayList<Integer>> postings = invertedIndex.get(t.toLowerCase());
// 		String postList = "[";
// 		//loops...
// 		postings.forEach((key, value) -> {
// 				String docList = "<" + key + " : ";
// 				ArrayList<Integer> vals = value;
// 				for (int i = 0; i < vals.size(); i++) {
// 					docList += vals.get(i);
// 					if (i != vals.size() - 1) {
// 						docList += ",";
// 					}
// 				}
// 				docList += ">";
// 				String finalList = postList + docList + ",";
// //				System.out.print(docList);
// 		});
// 		postList += "]";
// //		System.out.println(postList);
// 		return postList;
// 	}
	
	double weight(String t, String d) {
		// w(t,d) = sqrt(TFtd) * log10(N/DFt)
		return Math.sqrt(termFrequency(t, d)) * Math.log10( ((double) dataFolder.list().length / docFrequency(t)));
	}
	
	public double TPScore(String query, String doc) {
        String[] queryTerms = query.toLowerCase().split("\\s+");
        
        if (queryTerms.length == 1) {
            return 0.0;
        }
    
        double totalDistance = 0.0;
    
        for (int i = 0; i < queryTerms.length - 1; i++) {
            String term1 = queryTerms[i];
            String term2 = queryTerms[i + 1];
    
            double distance = distd(term1, term2, doc);
            totalDistance += distance;
        }
    
        return totalDistance;
    }
    
    private double distd(String term1, String term2, String doc) {
        if (!invertedIndex.containsKey(term1) || !invertedIndex.containsKey(term2)) {
            return 0.0;
        }
    
        ArrayList<Integer> positions1 = invertedIndex.get(term1).get(doc);
        ArrayList<Integer> positions2 = invertedIndex.get(term2).get(doc);
    
        if (positions1 == null || positions2 == null) {
            // One or both terms are not present in the document
            return 0.0;
        }
    
        // Calculate the minimum distance between the two terms in the document
        double minDistance = Double.MAX_VALUE;
    
        for (Integer pos1 : positions1) {
            for (Integer pos2 : positions2) {
                double distance = Math.abs(pos1 - pos2);
                minDistance = Math.min(minDistance, distance);
            }
        }
    
        return minDistance;
    }
	
	public double VSScore(String query, String doc) {
        // Extract all terms from the collection of documents
        Map<String, Integer> termFrequenciesInQuery = termFrequency(query, doc);
        Map<String, Double> weightsInQuery = calculateWeights(termFrequenciesInQuery);

        Map<String, Integer> termFrequenciesInDoc = getTermFrequencies(doc);
        Map<String, Double> weightsInDoc = calculateWeights(termFrequenciesInDoc);

        // Calculate the cosine similarity between the query vector and the document vector
        double cosineSimilarity = calculateCosineSimilarity(weightsInQuery, weightsInDoc);

        return cosineSimilarity;
    }

    private double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        // Calculate the cosine similarity between two vectors
        double dotProduct = 0.0;
        double normVector1 = 0.0;
        double normVector2 = 0.0;

        for (String term : vector1.keySet()) {
            dotProduct += vector1.get(term) * vector2.getOrDefault(term, 0.0);
            normVector1 += Math.pow(vector1.get(term), 2);
            normVector2 += Math.pow(vector2.getOrDefault(term, 0.0), 2);
        }

        if (normVector1 == 0.0 || normVector2 == 0.0) {
            // Prevent division by zero
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normVector1) * Math.sqrt(normVector2));
    }
	
	double Relevance(String query, String doc) {
		return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc); 
	}
}