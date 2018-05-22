package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class RootWindow extends Application {
    public static SerialCommunication serial = new SerialCommunication();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //byte[] at = new byte[] {0x41,0x54,0x5a,0xa};//{0x30,0x31,0x30,0x30,0xa};//{0x41,0x54,0x5a,0xa};
            //SerialPort communicationPort = serial.selectPort("ttyUSB0");
            //serial.openConnection(communicationPort,38400);

            //serial.sendData(communicationPort,at);
            //byte[] response = serial.waitAndReadData(communicationPort);
            //System.out.println(new String(response));
            //serial.closeConnection(communicationPort);

            AnchorPane root = FXMLLoader.load(RootWindow.class.getResource("/fxml/RootWindow.fxml"));
            primaryStage.setScene(new Scene(root, 700, 600));
            primaryStage.setTitle("Yeti by SpajsTech Ltd. 2018");
            primaryStage.show();
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
