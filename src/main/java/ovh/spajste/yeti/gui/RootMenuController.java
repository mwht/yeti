package ovh.spajste.yeti.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ovh.spajste.yeti.RootWindow;

import java.util.Arrays;
import java.util.Collections;

public class RootMenuController {
    @FXML
    private Menu portMenu;
    private String[] portsMenuNames = RootWindow.serial.getSerialPortsSystemNames();
    private Label rpmlabel;
    private boolean rescan = true;

    @FXML public void addPortMenuItem() {
        RootWindow.serial.rescanSerialPorts();
        if (rescan == true) {
            if (RootWindow.serial.getSerialPortsSystemNames().length != 0) {
                portMenu.getItems().remove(0, 1);
            }
            if (portsMenuNames.length == RootWindow.serial.getSerialPortsSystemNames().length) {
                if (!Collections.disjoint(Arrays.asList(portsMenuNames), Arrays.asList(RootWindow.serial.getSerialPortsSystemNames()))) {
                    for (String s : portsMenuNames) {
                        // System.out.println(s);
                        MenuItem temp = new MenuItem(s);
                        temp.setId(s);
                        temp.setOnAction(a->{
                            RootWindow.PortName = s;
                            RootWindow.elmInterface.initialize(s);
                        });
                        portMenu.getItems().add(temp);
                    }
                }
            }
            rescan = false;
        }
    }

    @FXML public void quitItem() {
        RootWindow.elmInterface.close();
        Platform.exit();
    }


    @FXML public void initialize()
    {
        addPortMenuItem();
    }

    @FXML public void startLogging(ActionEvent actionEvent) {
        RootWindow.elmInterface.startWriteToFile();
    }

    @FXML public void stopLogging(ActionEvent actionEvent) {
        RootWindow.elmInterface.stopWriteToFile();
    }

    @FXML public void onAboutClicked(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.setContentText("SpajsTech team are:\n" +
                "===================\n" +
                "\n" +
                "* Michał Łukiański (@Lukkario) - low-level serial communication, design\n" +
                "* Sebastian Madejski (@mwht) - core, ELM handling\n" +
                "* Jakub Politowski (@F1S2O5P) - readout calculations, design\n" +
                "* Adam Sylla (@adamski126) - GUI, readout calculations\n" +
                "\n" +
                "Special thanks to Wiktoria Sobczyk\n" +
                "\n" +
                "Yeti by SpajsTech\n" +
                "Copyright (c) SpajsTech 2018");
        dialog.setTitle("About Yeti/SpajsTech");
        dialog.showAndWait();
    }
}
