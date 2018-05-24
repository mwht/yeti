package ovh.spajste.yeti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.concurrent.*;

import java.io.IOException;
import java.util.List;

public class RootWindow extends Application {
    public static SerialCommunication serial = new SerialCommunication();
    public static ELMInterface elmInterface;
    public static AnchorPane root;
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(RootWindow.class.getResource("/fxml/RootWindow.fxml"));
            primaryStage.setScene(new Scene(root, 700, 600));
            primaryStage.setTitle("Yeti by SpajsTech Ltd. 2018");
            primaryStage.show();
            elmInterface = new ELMInterface();
            elmInterface.initialize("ttyUSB0");
            Task rpmTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					List<Readout> readouts;
					while(!isCancelled()) {
						readouts = elmInterface.getReadoutsData();
						updateTitle(readouts.get(0).getName()+": "+readouts.get(0).getValue()+readouts.get(0).getUnit());
						Thread.sleep(666);
					}
					return null;
				}
            	
            };
            primaryStage.titleProperty().bind(rpmTask.titleProperty());
            new Thread(rpmTask).start();
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
