package gui.controller.output;

import components.ComponentManager;
import gui.view.MainStage;
import gui.view.OutputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

import java.util.*;
import java.util.stream.Collectors;

public class SingleResultController implements EventHandler<ActionEvent>{

    private ListView<String> filesListView;
    private OutputView outputView;

    public SingleResultController(ListView<String> filesListView, OutputView outputView) {
        this.filesListView = filesListView;
        this.outputView = outputView;
    }

    @Override
    public void handle(ActionEvent event) {
        String fileName = this.filesListView.getSelectionModel().getSelectedItem();
        Map<String, Integer> resultMap = ComponentManager.getInstance().getResultForFile(fileName);

        Map<String, Integer> sortedMap = resultMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        List<Integer> frequencies = new ArrayList<>();
        for (Integer value: sortedMap.values()) {
            if (frequencies.size() >= 100) {
                break;
            }
            frequencies.add(value);
        }

        MainStage.getInstance().getGraphDistributionView().showGraph(fileName, frequencies);
    }
}
