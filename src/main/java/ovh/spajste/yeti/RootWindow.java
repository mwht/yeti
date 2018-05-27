package ovh.spajste.yeti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Yeti by SpajsTech Ltd. 2018");
            primaryStage.show();
            elmInterface = new ELMInterface();
            elmInterface.initialize("ttyUSB0");
            Task rpmTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					List<Readout> readouts;
					System.out.println("Task running...");
					while(!isCancelled()) {
						System.out.println("Reading ELM...");
						readouts = elmInterface.getReadoutsData();
						try {
							if(readouts.get(0).isActive()) {
								updateMessage(readouts.get(0).getValue()+" "+readouts.get(0).getUnit());
								System.out.println(readouts.get(0).getName()+": "+readouts.get(0).getValue()+" "+readouts.get(0).getUnit());
							} else {
								updateMessage("N/A");
							}
						} catch(Exception e) {
							updateMessage("N/A");
						}
						Thread.sleep(666);
					}
					elmInterface.close();
					System.out.println("Task ending...");
					return null;
				}
            	
            };
            Label rpmlabel = (Label) scene.lookup("#"+RPMReadout.class.getSimpleName()+"Value");             
            new Thread(rpmTask).start();
	    rpmlabel.textProperty().bind(rpmTask.messageProperty());
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
