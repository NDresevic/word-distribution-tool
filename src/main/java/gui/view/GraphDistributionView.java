package gui.view;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

import java.util.List;

public class GraphDistributionView extends HBox {

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart<Number, Number> lineChart;

    public GraphDistributionView() {
        super(5);
        initAndAddElements();
    }

    private void initAndAddElements() {
        xAxis = new NumberAxis(0, 100, 10);
        yAxis = new NumberAxis(0, 100, 10);
        lineChart = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Words");
        yAxis.setLabel("Frequency");

        setPadding(new Insets(5));
        getChildren().add(this.lineChart);
    }

    public void showGraph(String fileName, List<Integer> frequencies) {
        XYChart.Series series = new XYChart.Series();
        series.setName(fileName);

        yAxis = new NumberAxis(0, frequencies.get(0), frequencies.get(0) / 10);
        yAxis.setLabel("Frequency");
        lineChart = new LineChart<>(xAxis, yAxis);
        for (int i = 0; i < frequencies.size(); i++) {
            series.getData().add(new XYChart.Data(i, frequencies.get(i)));
        }

        this.lineChart.getData().add(series);
        getChildren().clear();
        getChildren().add(this.lineChart);
    }
}
