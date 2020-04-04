package gui.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainView extends BorderPane {

    public MainView() {
        initElements();
        addElements();
    }

    private void initElements() {
        setPadding(new Insets(30, 10, 20, 30));
    }

    private void addElements() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(InputView.getInstance(), new CruncherView());

        this.setLeft(hBox);
        this.setCenter(new GraphDistributionView());
        this.setRight(new OutputView());

//        BorderPane.setAlignment(this.getTop(), Pos.CENTER);

//        vBox.setAlignment(Pos.CENTER);
//		vbox.getChildren().addAll(username, password, submit);

//        this.register.setOnAction(e -> {
//            MainApp.window.setScene(new Scene(new RegisterView(), 400, 350));
//            MainApp.centerStage(MainApp.window);
//        });
    }
}
