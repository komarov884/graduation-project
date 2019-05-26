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

        scene.getStylesheets().add(UIConstants.CSS_FILE);

        primaryStage.setTitle(UIConstants.MAIN_FORM_TITLE);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
