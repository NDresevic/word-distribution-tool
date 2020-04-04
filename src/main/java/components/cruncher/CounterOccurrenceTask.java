package components.cruncher;

import components.ComponentManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class CounterOccurrenceTask extends RecursiveTask<Map<String, Integer>> {

    private static int L = ComponentManager.COUNTER_DATA_LIMIT;

    private int startIndex;
    private int endIndex;
    private String text;
    private int arity;

    public CounterOccurrenceTask(int startIndex, int endIndex, String text, int arity) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.text = text;
        this.arity = arity;
    }

    @Override
    protected Map<String, Integer> compute() {
        if (startIndex >= endIndex) {
            return new ConcurrentHashMap<>();
        }
        if (endIndex - startIndex <= L) {
            return computeOccurrence(startIndex, endIndex);
        }

        int right = startIndex + L;
        if (right >= endIndex) {
            right = endIndex;
        } else {
            while (right > startIndex && this.text.charAt(right) != ' ') {
                right--;
            }
        }

        CounterOccurrenceTask forkJob = new CounterOccurrenceTask(startIndex, right, text, arity);

        // take (arity - 1) words to the left
        int left = right + 1;
        if (left > 0 && arity > 1) {
            for (int i = 0; i < arity; i++) {
                while (left > 0 && this.text.charAt(left) != ' ') {
                    left--;
                }
                if (left <= 0) {
                    break;
                }
                left--;
            }
            left += 2;
        }

        CounterOccurrenceTask computeJob = new CounterOccurrenceTask(left, endIndex, text, arity);
        forkJob.fork();
        Map<String, Integer> rightResult = computeJob.compute();
        Map<String, Integer> leftResult = forkJob.join();

        // merge left and right result into right
        leftResult.forEach((key, value) -> {
            if (rightResult.containsKey(key)) {
                int rightValue = rightResult.get(key);
                rightResult.put(key, value + rightValue);
            } else {
                rightResult.put(key, value);
            }
        });

        return rightResult;
    }

    private Map<String, Integer> computeOccurrence(int startIndex, int endIndex) {
        Map<String, Integer> result = new ConcurrentHashMap<>();

        List<String> words = this.textToWords(startIndex, endIndex);
        List<String> originalBag = new ArrayList<>();
        List<String> sortedBag = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            originalBag.add(words.get(i));
            sortedBag.add(words.get(i));
            if (i < arity - 1) {
                continue;
            }

            Collections.sort(sortedBag);
            String key = this.listToString(sortedBag);
            if (result.containsKey(key)) {
                int value = result.get(key);
                result.put(key, value + 1);
            } else {
                result.put(key, 1);
            }
            sortedBag.remove(originalBag.remove(0));
        }

        return result;
    }

    private List<String> textToWords(int startIndex, int endIndex) {
        List<String> words = new ArrayList<>();
        int begin = startIndex;
        for (int i = startIndex; i < endIndex; i++) {
            if (!Character.isWhitespace(this.text.charAt(i))) {
                continue;
            }
            words.add(this.text.substring(begin, i).intern());
            i++;
            begin = i;
        }
        words.add(this.text.substring(begin, endIndex).intern());
        return words;
    }

    private String listToString(List<String> words) {
        if (arity == 1) {
            return words.get(0);
        }
        StringBuilder result = new StringBuilder(words.get(0));
        for (int i = 1; i < arity; i++) {
            result.append(" " + words.get(i));
        }
        return result.toString();
    }
}
