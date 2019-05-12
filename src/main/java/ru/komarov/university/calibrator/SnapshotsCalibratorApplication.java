package ru.komarov.university.calibrator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.komarov.university.calibrator.util.UIConstants;

import java.net.URL;

/**
 * <p>
 * Created on 5/12/2019.
 *
 * @author Vasilii Komarov
 */
public class SnapshotsCalibratorApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlFile = ClassLoader.getSystemClassLoader().getResource(UIConstants.MAIN_FORM_FXML);

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        Pane root = fxmlLoader.load();

        Scene scene = new Scene(root);

        URL cssFile = ClassLoader.getSystemClassLoader().getResource(UIConstants.CSS_FILE);
        scene.getStylesheets().add((cssFile).toExternalForm()); //TODO !

        primaryStage.setTitle(UIConstants.MAIN_FORM_TITLE);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /* TODO !
    public class Reader {
        public static void main(String[] args) throws IOException {
            InputStream inputStream = new FileInputStream(ClassLoader.getSystemClassLoader().getResource("test-mbv.mbv").getFile());
            byte[] b = new byte[2];
            inputStream.read(b);
            int sdf = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8); // value of two bytes. There is incorrect bytes order in file!!!!!!
        }


        1000000001

        inputStream.read(b);
        int sdf = ((b[0] & 0xff) << 8) | (b[1] & 0xff);

        int sdf = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8);

        32769
        x
        8193

    }
    */

}
