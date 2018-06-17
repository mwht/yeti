package ovh.spajste.yeti.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
        Platform.exit();
    }


    @FXML public void initialize()
    {
        addPortMenuItem();
    }
}
