import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionalIndex {
    private String documentCollectionFileName = "";
    private Map<String, Map<String, List<Integer>>> PositionalIndex = new HashMap<>();
    private Map<String, float[]> vectorSpaceMap = new HashMap<>();
    private List<String> terms = new ArrayList<>();

    // /**
    // COULD USE BUT UNKNOWNN USAGE ABILITY
    //  * A cache of query vectors, so that we don't have to recompute the query vector for every
    //  * document comparison
    //  */
    // private Map<String, float[]> queryVectors = new ConcurrentHashMap<>();

    PositionalIndex(String filePathName){

        // TODO

         documentCollectionFileName = filePathName;
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

}