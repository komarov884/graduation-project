package ru.komarov.university.calibrator.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private Button btOpen;

    @FXML
    private Canvas testCanvas;

    @FXML
    private Label label;

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("MBV-files", "*.mbv"));
        //fileChooser.setInitialDirectory();//TODO
        File file = fileChooser.showOpenDialog(null); //TODO null?
        label.setText(file.getAbsolutePath());


        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[2];
            inputStream.read(b);
            int width = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8); // value of two bytes. There is incorrect bytes order in file!!!!!!
            inputStream.read(b);
            int height = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8); // value of two bytes. There is incorrect bytes order in file!!!!!!

            int[][] pixelsMap = new int[width][height];

            GraphicsContext gc = testCanvas.getGraphicsContext2D();

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    inputStream.read(b);
                    pixelsMap[j][i] = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8);
                    int color = pixelsMap[j][i] / 256;
                    gc.setFill(Color.rgb(color, color, color));
                    gc.fillRect(i, j, 1, 1);
                }
            }
            System.out.println();



        } catch (IOException e) {
            e.printStackTrace();
        }



    }

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
