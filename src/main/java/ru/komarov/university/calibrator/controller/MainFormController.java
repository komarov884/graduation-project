package ru.komarov.university.calibrator.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

/**
 * <p>
 * Created on 5/12/2019.
 *
 * @author Vasilii Komarov
 */
public class MainFormController {

    private static final Random random = new Random(System.currentTimeMillis()); //TODO !

    @FXML
    private Canvas testCanvas;

    @FXML
    public void adf() { //TODO !
        GraphicsContext gc = testCanvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)).toString()));
        for (int i = 0; i < 500; i++) {
            gc.fillRect(i, i, 1, 1);
            gc.fillRect(i + 1, i, 1, 1);
            gc.fillRect(i + 2, i, 1, 1);
            gc.fillRect(i + 3, i, 1, 1);

        }
    }
}
