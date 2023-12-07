import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryProcessor {

    private PositionalIndex positionalIndex;

    public QueryProcessor(PositionalIndex positionalIndex) {
        this.positionalIndex = positionalIndex;
    }

    public List<String> topKDocs(String query, int k) {
        // Calculate relevance scores for all documents
        Map<String, Double> relevanceScores = calculateRelevanceScores(query);

        // Sort documents by relevance score in descending order
        List<String> sortedDocs = relevanceScores.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Return the top k documents
        return sortedDocs.subList(0, Math.min(k, sortedDocs.size()));
    }

    private Map<String, Double> calculateRelevanceScores(String query) {
        // Calculate relevance scores for all documents based on the query
        Map<String, Double> relevanceScores = positionalIndex.calculateRelevanceScores("your_query_here");

        return relevanceScores;
    }

    
    public static void main(String[] args) {
        // Example usage
        String dataFolderPath = "/Users/rambekar/CS_435/CS_435_PA3/data/IR";  // Replace with the actual path
        PositionalIndex positionalIndex = new PositionalIndex(dataFolderPath);

        QueryProcessor queryProcessor = new QueryProcessor(positionalIndex);

        String query = "striking out nine and receiving pirates ball hit";
        int k = 5;  // Replace with the desired value of k

        List<String> topKDocuments = queryProcessor.topKDocs(query, k);

        // Display the top k documents
        System.out.println("Top " + k + " documents for the query \"" + query + "\":");
        for (String doc : topKDocuments) {
            System.out.println(doc);
        }
    }
}
