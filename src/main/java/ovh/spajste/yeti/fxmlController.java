package ovh.spajste.yeti;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class fxmlController {
    @FXML
    private Menu portMenu;

    @FXML public void addPortMenuItem() {

        RootWindow.serial.rescanSerialPorts();
        String[] portsMenuNames = RootWindow.serial.getSerialPortsSystemNames();

        for (String s : portsMenuNames) {
            portMenu.getItems().add(new MenuItem(s));
        }
    }

    @FXML public void clearPortMenuItem()
    {
        portMenu.getItems().remove(1,portMenu.getItems().size());
    }
}
