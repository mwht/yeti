package ovh.spajste.yeti;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    private Map<Class<?>,CheckBox> activeMap;
    public static int frames = 0;
    private long startingTime = 0;

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
            //elmInterface.initialize("COM6");
            labelMap = new HashMap<>();
            activeMap = new HashMap<>();
            EventHandler handleActive = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (event.getSource() instanceof CheckBox) {
                        CheckBox chk = (CheckBox) event.getSource();
                        activeMap.forEach((readout,chkbox) -> {
                            if(chkbox == chk) {
                                Readout r = (Readout) readout.cast(Readout.class);
                                r.setActive(chk.isSelected());
                            }
                        });
                    }
                }
            };

            Readouts.readoutMap.forEach((pid,readout) -> {
                Label readoutLabel = (Label) scene.lookup("#"+readout.getSimpleName()+"Value");
                labelMap.put(readout,readoutLabel);
                CheckBox readoutActive = (CheckBox) scene.lookup("#"+readout.getSimpleName()+"Active");
                activeMap.put(readout,readoutActive);
                readoutActive.setOnAction(handleActive);
            });


            LineChart line = (LineChart) scene.lookup("#readoutsChart");
            line.getXAxis().setLabel("Time [ms]");
            line.getYAxis().setLabel("Value [rpm]");
            line.getXAxis().setAutoRanging(true);
            line.setTitle("RPM Readout");

            XYChart.Series series = new XYChart.Series();
            line.getData().add(series);

            AnimationTimer timer = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    labelMap.forEach((labelClass,label) -> {
                        if(label != null)
                            elmInterface.getReadoutsData().forEach((readout) -> {
                                if(readout.getClass() == labelClass) {
                                    try {
                                        if(readout.isActive()) {
                                            label.setText(readout.getValue() + " " + readout.getUnit());
                                        } else {
                                            label.setText("-");
                                        }
                                    } catch(InvalidReadoutException ine) {
                                        label.setText("N/A");
                                        System.err.println(ine.getMessage());
                                    }
                                }
                            });
                    });

                    if(startingTime == 0) startingTime = now;
                    if(frames > 20) {
                        //series.getData().remove(0);
                        elmInterface.getReadoutsData().forEach((readout) -> {
                            if(readout.getClass() == RPMReadout.class) {
                                try {
                                    series.getData().add(new XYChart.Data((now - startingTime) / 1000000, readout.getValue()));
                                } catch(InvalidReadoutException ine) {
                                    System.err.println("Error adding point to chart: " + ine.getMessage());
                                }
                            }
                        });
                        cycle++;
                        frames = 0;
                    }
                    frames++;
                }

            };
            timer.start();

        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
