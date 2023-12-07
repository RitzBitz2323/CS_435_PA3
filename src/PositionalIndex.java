
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    Map<String, Double> vectorQuery = calculateVector(query);
    Map<String, Double> vectorDoc = calculateVector(doc);
    
    // Check for common terms in both vectors
    Set<String> commonTerms = new HashSet<>(vectorQuery.keySet());
    commonTerms.retainAll(vectorDoc.keySet());

    // If there are no common terms, cosine similarity is undefined, return 0
    if (commonTerms.isEmpty()) {
        return 0.0;
    }

    // Create new vectors with only common terms
    Map<String, Double> commonVectorQuery = new HashMap<>();
    Map<String, Double> commonVectorDoc = new HashMap<>();

    for (String term : commonTerms) {
        commonVectorQuery.put(term, vectorQuery.get(term));
        commonVectorDoc.put(term, vectorDoc.get(term));
    }

    // Calculate the cosine similarity between the common vectors
    double cosineSimilarity = cosSim(commonVectorQuery, commonVectorDoc);
    return cosineSimilarity;
}

    
    private Map<String, Double> calculateVector(String text) {
        Map<String, Integer> termFrequencies = getTermFrequencies(text);
        Map<String, Double> weights = calculateWeights(termFrequencies);
    
        return weights;
    }
    
    public double cosSim(Map<String, Double> vector1, Map<String, Double> vector2) {
        // Check if both vectors have the same dimensionality
        if (!vector1.keySet().equals(vector2.keySet())) {
            throw new IllegalArgumentException(
                    "Vectors must have the same dimensionality to compute cosine similarity");
        }
    
        // Check if vector dimensionality is greater than 0
        if (vector1.isEmpty()) {
            throw new IllegalArgumentException("Vector dimensionality must be greater than 0");
        }
    
        double numerator = 0;
        double a2Sum = 0;
        double b2Sum = 0;
    
        // Iterate over the common terms in both vectors
        for (String term : vector1.keySet()) {
            double value1 = vector1.get(term);
            double value2 = vector2.getOrDefault(term, 0.0);
    
            numerator += value1 * value2;
            a2Sum += Math.pow(value1, 2);
            b2Sum += Math.pow(value2, 2);
        }
    
        double denominator = Math.sqrt(a2Sum) * Math.sqrt(b2Sum);
    
        if (denominator == 0) {
            return 0;
        }
    
        return numerator / denominator;
    }
    
    
    private Map<String, Integer> getTermFrequencies(String text) {
        // Count the frequency of each term in the given text
        Map<String, Integer> termFrequencies = new HashMap<>();
        String[] terms = text.toLowerCase().split("\\s+");
    
        for (String term : terms) {
            termFrequencies.put(term, termFrequencies.getOrDefault(term, 0) + 1);
        }
    
        return termFrequencies;
    }
    
    private Map<String, Double> calculateWeights(Map<String, Integer> termFrequencies) {
        // Calculate the weights of each term based on the given formula
        Map<String, Double> weights = new HashMap<>();
        int totalDocuments = dataFolder.list().length;
    
        for (String term : termFrequencies.keySet()) {
            int termFrequency = termFrequencies.get(term);
            int documentFrequency = docFrequency(term);
    
            double weight = Math.sqrt(termFrequency) * Math.log10((double) totalDocuments / documentFrequency);
            weights.put(term, weight);
        }
    
        return weights;
    }
    
    public int docFrequency(String term) {
        // Get the document frequency of the term in the collection
        if (invertedIndex.containsKey(term)) {
            return invertedIndex.get(term).size();
        } else {
            // Term is not present in the inverted index
            return 0;
        }
    }
    

    
	
	double Relevance(String query, String doc) {
		return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc); 
	}
}