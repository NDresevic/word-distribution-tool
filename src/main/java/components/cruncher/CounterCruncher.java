package components.cruncher;

import components.input.InputData;
import components.output.CacheOutput;
import components.output.OutputData;

public interface CounterCruncher extends Runnable {

    void addInputDataToQueue(InputData inputData);
    void sendProcessedDataToOutput(OutputData outputData);

    void addOutput(CacheOutput cacheOutput);
    void removeOutput(CacheOutput cacheOutput);
}
