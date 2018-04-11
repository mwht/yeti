package ovh.spajste;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args )
    {
     launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root;
        try {
            //root = new FXMLLoader().load(new FileInputStream("D:\\ProjektyJava\\obdsm-2\\src\\main\\resources\\RootWindow.fxml"));
            root = (AnchorPane) FXMLLoader.load(App.class.getResource("/App.fxml"));
            primaryStage.setTitle("OBDSM-II");
            primaryStage.setScene(new Scene(root,500,500));
            primaryStage.show();
        } catch(Exception e) {
            System.out.println("chuj a nie programista - "+e.getClass().getName()+" : "+e.getMessage());
        }
    }
}
