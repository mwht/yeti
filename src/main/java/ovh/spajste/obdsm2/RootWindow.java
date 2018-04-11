package ovh.spajste.obdsm2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RootWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = FXMLLoader.load(RootWindow.class.getResource("/RootWindow.fxml"));
            primaryStage.setScene(new Scene(root, 700, 450));
            primaryStage.setTitle("OBDSM-II by SpajsTech Ltd. 2018");
            primaryStage.show();
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
