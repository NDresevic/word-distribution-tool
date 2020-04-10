package components.output;

import java.util.Map;
import java.util.concurrent.Future;

public class OutputData {

    /**
     * Name of the file.
     */
    private String name;
    /**
     * [bag of words -> number of occurrence in the given file]
     */
    private Future<Map<String, Integer>> bagOfWordsOccurrenceMap;
    /**
     * Boolean that indicates if this object is used as a poison pill.
     */
    private boolean poisoned;

    public OutputData(String name, Future<Map<String, Integer>> bagOfWordsOccurrenceMap) {
        this.name = name;
        this.bagOfWordsOccurrenceMap = bagOfWordsOccurrenceMap;
        this.poisoned = false;
    }

    public OutputData(boolean poisoned) {
        this.name = "";
        this.bagOfWordsOccurrenceMap = null;// new HashMap<String, Integer>();
        this.poisoned = poisoned;
    }

    public String getName() {
        return name;
    }

    public Future<Map<String, Integer>> getBagOfWordsOccurrenceMap() {
        return bagOfWordsOccurrenceMap;
    }

    public boolean isPoisoned() {
        return poisoned;
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
