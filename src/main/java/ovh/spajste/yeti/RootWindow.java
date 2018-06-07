package ovh.spajste.yeti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    public static int cycle = 0;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Readouts.init();
            root = FXMLLoader.load(RootWindow.class.getResource("/fxml/RootWindow.fxml"));
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Yeti by SpajsTech Ltd. 2018");
            primaryStage.show();
            //elmInterface = new ELMInterface();
            //elmInterface.initialize("ttyUSB0");
            Task readoutTask = new Task<Void>() {

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
								updateMessage("???");
							}
						} catch(Exception e) {
                            System.err.println("Exception in readoutTask: "+e.getClass().getSimpleName()+": "+e.getMessage());
						}
						Thread.sleep(666);
					}
					elmInterface.close();
					System.out.println("Task ending...");
					return null;
				}
            	
            };
            Label rpmlabel = (Label) scene.lookup("#"+ThrottlePositionReadout.class.getSimpleName()+"Value");
            //new Thread(readoutTask).start();
    	    rpmlabel.textProperty().bind(readoutTask.messageProperty());

    	    LineChart line = (LineChart) scene.lookup("#readoutsChart");
    	    line.getXAxis().setLabel("Time [ms]");
    	    line.getYAxis().setLabel("Value [%]");
            XYChart.Series series = new XYChart.Series();
            Task fifo = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    for(int i=0;i<10;i++) {
                        series.getData().add(new XYChart.Data(i+cycle,Math.sin(i+cycle)));
                        cycle = 10;
                    }
                    while(!isCancelled()) {
                        series.getData().remove(0);
                        series.getData().add(new XYChart.Data(cycle,Math.sin(cycle)));
                        cycle++;
                        Thread.sleep(666);
                    }
                    return null;
                }

            };
            line.getData().add(series);
            new Thread(fifo).start();
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
