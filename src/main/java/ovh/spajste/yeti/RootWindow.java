package ovh.spajste.yeti;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RootWindow extends Application {
    public static SerialCommunication serial = new SerialCommunication();
    public static ELMInterface elmInterface;
    public static AnchorPane root;
    public static int cycle = 0;
    private Map<Class<?>,Label> labelMap;

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
            elmInterface = new ELMInterface();
            //elmInterface.initialize("COM3");
            labelMap = new HashMap<>();
            Readouts.readoutMap.forEach((pid,readout) -> {
                Label readoutLabel = (Label) scene.lookup("#"+readout.getSimpleName()+"Value");
                labelMap.put(readout,readoutLabel);
            });

            CheckBox throttleCheckbox = (CheckBox) scene.lookup("#ThrottlePositionReadoutActive");
            throttleCheckbox.setSelected(true);
            throttleCheckbox.setDisable(false);

            AnimationTimer timer = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    labelMap.forEach((labelClass,label) -> {
                        if(label != null)
                            elmInterface.getReadoutsData().forEach((readout) -> {
                                if(readout.getClass() == labelClass) {
                                    try {
                                        label.setText(readout.getValue() + " " + readout.getUnit());
                                    } catch(InvalidReadoutException ine) {
                                        label.setText("N/A");
                                        System.err.println(ine.getMessage());
                                    }
                                }
                            });
                    });
                }

            };
            timer.start();
/*            LineChart line = (LineChart) scene.lookup("#readoutsChart");
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
            new Thread(fifo).start();*/
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
