package gui.controller.output;

import components.ComponentManager;
import gui.view.MainStage;
import gui.view.OutputView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.*;
import java.util.stream.Collectors;

public class SingleResultController implements EventHandler<ActionEvent>{

    private ListView<String> filesListView;
    private OutputView outputView;
    private int K;

    public SingleResultController(ListView<String> filesListView, OutputView outputView) {
        this.filesListView = filesListView;
        this.outputView = outputView;
        this.K = ComponentManager.SORT_PROGRESS_LIMIT;
    }

    @Override
    public void handle(ActionEvent event) {
        String fileName = this.filesListView.getSelectionModel().getSelectedItem();
        Map<String, Integer> resultMap = ComponentManager.getInstance().getOutput().poll(fileName);

        if (resultMap == null) {
            Alert newAlert = new Alert(Alert.AlertType.ERROR, "Result is not ready.", ButtonType.OK);
            newAlert.showAndWait();
            return;
        }

        new Thread(() -> {
            final double updateValue = (resultMap.size()*Math.log(resultMap.size()) / K) / 100;
            final int[] count = new int[1];

            Map<String, Integer> sortedMap = resultMap
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder((Map.Entry e1, Map.Entry e2) -> {
                        count[0]++;
                        if (count[0] % K == 0) {
//                            Platform.runLater(() -> this.outputView.updateProgressBar(updateValue));
                            this.outputView.updateProgressBar(updateValue);
                        }

                        return Integer.compare((int) e1.getValue(), (int) e2.getValue());
                    }))
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

            List<Integer> frequencies = new ArrayList<>();
            for (Integer value: sortedMap.values()) {
                if (frequencies.size() >= 100) {
                    break;
                }
                frequencies.add(value);
            }

            Platform.runLater(() -> this.outputView.hideAndResetProgressBar());
            MainStage.getInstance().getGraphDistributionView().showGraph(fileName, frequencies);
        }).run();
    }
}
