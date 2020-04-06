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

    public Future<Map<String, Integer>> getBagOfWordsOccurrenceMap() {
        return bagOfWordsOccurrenceMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OutputData) {
            OutputData otherObj = (OutputData) obj;
            return this.name.equals(otherObj.getName());
        }
        return false;
    }
}
