package components.output;

import java.util.Map;
import java.util.concurrent.Future;

public class OutputData {

    private String name;
    private Future<Map<String, Integer>> bagOfWordsOccurrenceMap;

    public OutputData(String name, Future<Map<String, Integer>> bagOfWordsOccurrenceMap) {
        this.name = name;
        this.bagOfWordsOccurrenceMap = bagOfWordsOccurrenceMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Future<Map<String, Integer>> getBagOfWordsOccurrenceMap() {
        return bagOfWordsOccurrenceMap;
    }
}
