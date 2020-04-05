package gui.view;

import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

public class GraphDistributionView extends HBox {

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private ScatterChart<Number, Number> scatterChart;

    public GraphDistributionView() {
        super(5);
        initElements();
        addElements();
    }

    private void initElements() {
        this.xAxis = new NumberAxis(0, 110, 5);
        this.yAxis = new NumberAxis(0, 110, 5);
        this.scatterChart = new ScatterChart<>(xAxis,yAxis);
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        setPadding(new Insets(5));
    }

    private void addElements() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Equities");
        series.getData().add(new XYChart.Data(4.2, 193.2));
        series.getData().add(new XYChart.Data(2.8, 33.6));
        series.getData().add(new XYChart.Data(6.2, 24.8));

        this.scatterChart.getData().add(series);
        getChildren().add(this.scatterChart);
    }
}
