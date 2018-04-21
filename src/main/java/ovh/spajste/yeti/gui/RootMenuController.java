package ovh.spajste.yeti.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ovh.spajste.yeti.RootWindow;

public class RootMenuController {
    @FXML
    private Menu rootMenu;

    @FXML public void addPortMenuItem() {

        RootWindow.serial.rescanSerialPorts();
        String[] portsMenuNames = RootWindow.serial.getSerialPortsSystemNames();

        for (String s : portsMenuNames) {
            rootMenu.getItems().add(new MenuItem(s));
        }
    }

    @FXML public void clearPortMenuItem()
    {
        rootMenu.getItems().remove(1,rootMenu.getItems().size());
    }

    @FXML public void quitItem() {
        Platform.exit();
    }
}
