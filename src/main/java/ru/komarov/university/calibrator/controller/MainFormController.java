package ru.komarov.university.calibrator.controller;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.StatusBar;
import org.controlsfx.dialog.ProgressDialog;
import ru.komarov.university.calibrator.calibrator.Calibrator;
import ru.komarov.university.calibrator.calibrator.CalibratorImpl;
import ru.komarov.university.calibrator.domain.Calibration;
import ru.komarov.university.calibrator.domain.ReferenceSnapshot;
import ru.komarov.university.calibrator.domain.Snapshot;
import ru.komarov.university.calibrator.drawer.Drawer;
import ru.komarov.university.calibrator.enums.ReferenceSnapshotType;
import ru.komarov.university.calibrator.exception.SnapshotParsingException;
import ru.komarov.university.calibrator.util.ColorUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 * Created on 5/12/2019.
 *
 * @author Vasilii Komarov
 */
public class MainFormController implements Initializable {

    private static final String STATUS_BAR_ELEMENT_SEPARATOR = "   |   ";
    private static final String STATUS_BAR_X_TEMPLATE = "x: %5s";
    private static final String STATUS_BAR_Y_TEMPLATE = "y: %5s";
    private static final String STATUS_BAR_CODE_TEMPLATE = "Код: %5s";

    @FXML
    private Canvas canvasTop;
    @FXML
    private Canvas canvasBottom;
    @FXML
    private Canvas canvasSpectrum;

    @FXML
    private Label labelTopCanvas;
    @FXML
    private Label labelBottomCanvas;

    @FXML
    private Label labelRangeValue;
    @FXML
    private Label labelXTopLowestAreaValue;
    @FXML
    private Label labelYTopLowestAreaValue;
    @FXML
    private Label labelOriginalCodeTopLowestAreaValue;
    @FXML
    private Label labelXBottomLowestAreaValue;
    @FXML
    private Label labelYBottomLowestAreaValue;
    @FXML
    private Label labelOriginalCodeBottomLowestAreaValue;
    @FXML
    private Label labelTargetCodeLowestTempAreaValue;
    @FXML
    private Label labelXTopHighestAreaValue;
    @FXML
    private Label labelYTopHighestAreaValue;
    @FXML
    private Label labelOriginalCodeTopHighestAreaValue;
    @FXML
    private Label labelXBottomHighestAreaValue;
    @FXML
    private Label labelYBottomHighestAreaValue;
    @FXML
    private Label labelOriginalCodeBottomHighestAreaValue;
    @FXML
    private Label labelTargetCodeHighestTempAreaValue;

    @FXML
    private ListView<Snapshot> listViewSnapshots;
    @FXML
    private ListView<Calibration> listViewCalibrations;

    @FXML
    private StatusBar statusBar;

    private String initialDirectory;

    private Label labelX;
    private Label labelY;
    private Label labelCode;

    private GraphicsContext canvasTopGc;
    private GraphicsContext canvasBottomGc;
    private GraphicsContext canvasSpectrumGc;

    private Drawer drawer; //TODO: extract interface
    private Calibrator calibrator;

    private Map<Integer, Snapshot> snapshots;
    private List<Calibration> calibrations;

    private ReferenceSnapshot topReferenceSnapshot;
    private ReferenceSnapshot bottomReferenceSnapshot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasTopGc = canvasTop.getGraphicsContext2D();
        canvasBottomGc = canvasBottom.getGraphicsContext2D();
        canvasSpectrumGc = canvasSpectrum.getGraphicsContext2D();

        drawer = new Drawer();
        calibrator = new CalibratorImpl();
        snapshots = new HashMap<>();
        calibrations = new ArrayList<>();

        listViewCalibrations.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        getCalibrationChangeListener()
                );

        setInitialDirectory();
        clearLabels();
        clearCanvases();
        drawSpectrum();
        initializeStatusBar();
    }

    private void setInitialDirectory() {
        initialDirectory = System.getProperty("user.dir");
        String ws = System.getProperty("ws");
        if (Objects.nonNull(ws)) {
            if (WorkStation.HOME.name().equalsIgnoreCase(ws)) {
                initialDirectory = "D:\\Documents\\Универ\\ВКР (диплом)\\500 ИК файлов формата mbv";
            } else if (WorkStation.LAPTOP.name().equalsIgnoreCase(ws)) {
                initialDirectory = "C:\\Users\\komar\\Desktop\\GW\\500 ИК файлов формата mbv";
            } else if (WorkStation.WORK.name().equalsIgnoreCase(ws)) {
                initialDirectory = "C:\\Users\\Vasilii_Komarov\\Desktop\\university\\ВКР (диплом)\\500 ИК файлов формата mbv";
            }
        }
    }

    @FXML
    public void btOpenOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MBV-files", "*.mbv"));
        fileChooser.setInitialDirectory(new File(initialDirectory));
        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (CollectionUtils.isNotEmpty(files)) {
            btClearOnAction();
            Task filesPreprocessing = createFilesPreprocessing(files);
            ProgressDialog progressDialog = new ProgressDialog(filesPreprocessing);
            progressDialog.setContentText("Обработано:");
            progressDialog.setTitle("Загрузка снимков");
            progressDialog.setHeaderText("Выполняется предобработка снимков");
            progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            progressDialog.setOnCloseRequest(event -> filesPreprocessing.cancel());
            Thread thread = new Thread(filesPreprocessing);
            thread.start();
            progressDialog.showAndWait();

            if (filesPreprocessing.isCancelled()) {
                btClearOnAction();
            } else {
                snapshots.values()
                        .forEach(snapshot -> listViewSnapshots.getItems().add(snapshot));
                selectTopReferenceSnapshot(snapshots.get(1)); //todo INVESTIGATE it!!!
                selectBottomReferenceSnapshot(snapshots.get(snapshots.size()));
            }
        }
    }

    @FXML
    public void btClearOnAction() {
        listViewSnapshots.getItems().clear();
        listViewCalibrations.getItems().clear();
        snapshots.clear();
        calibrations.clear();
        topReferenceSnapshot = null;
        bottomReferenceSnapshot = null;
        clearLabels();
        clearCanvases();
    }

    @FXML
    public void btSelectTopCanvasOnAction() {
        if (MapUtils.isEmpty(snapshots)) { // TODO: refactoring. DRY
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет загруженных снимков");
            alert.setHeaderText("Невозможно выполнить операцию");
            alert.setContentText("Необходимо загрузить снимки");
            alert.showAndWait();
        } else {
            TextInputDialog dialog = new TextInputDialog(
                    String.valueOf(
                            snapshots.get(1).getNumber()
                    )
            );

            dialog.setTitle("Выбрать");
            dialog.setHeaderText("Введите номер снимка:");
            dialog.setContentText("Номер снимка:");
            TextField editor = dialog.getEditor();
            editor.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) { //TODO: leading zeros
                    editor.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                Snapshot snapshot = snapshots.get(Integer.valueOf(result.get()));
                if (Objects.isNull(snapshot)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Нет снимка с таким номером");
                    alert.setHeaderText("Невозможно выполнить операцию");
                    alert.setContentText("Выберите существующий снимок");
                    alert.showAndWait();
                } else {
                    selectTopReferenceSnapshot(snapshot);
                }
            });
        }
    }

    @FXML
    public void btSelectBottomCanvasOnAction() {
        if (MapUtils.isEmpty(snapshots)) { // TODO: refactoring. DRY
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет загруженных снимков");
            alert.setHeaderText("Невозможно выполнить операцию");
            alert.setContentText("Необходимо загрузить снимки");
            alert.showAndWait();
        } else {
            TextInputDialog dialog = new TextInputDialog(
                    String.valueOf(
                            snapshots.get(snapshots.size()).getNumber()
                    )
            );

            dialog.setTitle("Выбрать");
            dialog.setHeaderText("Введите номер снимка:");
            dialog.setContentText("Номер снимка:");
            TextField editor = dialog.getEditor();
            editor.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) { //TODO: leading zeros
                    editor.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                Snapshot snapshot = snapshots.get(Integer.valueOf(result.get()));
                if (Objects.isNull(snapshot)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Нет снимка с таким номером");
                    alert.setHeaderText("Невозможно выполнить операцию");
                    alert.setContentText("Выберите существующий снимок");
                    alert.showAndWait();
                } else {
                    selectBottomReferenceSnapshot(snapshot);
                }
            });
        }
    }

    @FXML
    public void btSaveOnAction() {
        //TODO: implement
    }

    @FXML
    public void btCalibrateOnAction() {
        if (Objects.nonNull(topReferenceSnapshot)
                && Objects.nonNull(topReferenceSnapshot.getLowestTempPoint())
                && Objects.nonNull(topReferenceSnapshot.getHighestTempPoint())
                && Objects.nonNull(bottomReferenceSnapshot)
                && Objects.nonNull(bottomReferenceSnapshot.getLowestTempPoint())
                && Objects.nonNull(bottomReferenceSnapshot.getHighestTempPoint())) {

            //TODO: some checks! ! ! ! что калибровка начинается строго с первого снимка, что калибровки идут с перекрытием
            //TODO: bottom не может быть раньше чем top. и не могут быть одним и тем же.

            Snapshot topSnapshot = topReferenceSnapshot.getSnapshot();
            Snapshot bottomSnapshot = bottomReferenceSnapshot.getSnapshot();
            Point2D topLowestTempPoint = topReferenceSnapshot.getLowestTempPoint();
            Point2D topHighestTempPoint = topReferenceSnapshot.getHighestTempPoint();
            Point2D bottomLowestTempPoint = bottomReferenceSnapshot.getLowestTempPoint();
            Point2D bottomHighestTempPoint = bottomReferenceSnapshot.getHighestTempPoint();

            int maxLowestTempAreaCode = topSnapshot.getAreaAverageCode(topLowestTempPoint, Calibration.AREA_THRESHOLD);
            int minHighestTempAreaCode = topSnapshot.getAreaAverageCode(topHighestTempPoint, Calibration.AREA_THRESHOLD);

            Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap = new HashMap<>();
            snapshotIdReferencePointsMap.put(topSnapshot.getNumber(), new ImmutablePair<>(topLowestTempPoint, topHighestTempPoint));

            for (int i = topSnapshot.getNumber() + 1; i <= bottomSnapshot.getNumber(); i++) {
                Snapshot snapshot = snapshots.get(i);
                double xLowestTempPoint = (((bottomLowestTempPoint.getX() - topLowestTempPoint.getX()) * (i - topSnapshot.getNumber())) / (bottomSnapshot.getNumber() - topSnapshot.getNumber())) + topLowestTempPoint.getX(); //TODO: refactoring
                double yLowestTempPoint = (((bottomLowestTempPoint.getY() - topLowestTempPoint.getY()) * (i - topSnapshot.getNumber())) / (bottomSnapshot.getNumber() - topSnapshot.getNumber())) + topLowestTempPoint.getY();
                int lowestTempAreaCode = snapshot.getAreaAverageCode(xLowestTempPoint, yLowestTempPoint, Calibration.AREA_THRESHOLD);
                double xHighestTempPoint = (((bottomHighestTempPoint.getX() - topHighestTempPoint.getX()) * (i - topSnapshot.getNumber())) / (bottomSnapshot.getNumber() - topSnapshot.getNumber())) + topHighestTempPoint.getX();
                double yHighestTempPoint = (((bottomHighestTempPoint.getY() - topHighestTempPoint.getY()) * (i - topSnapshot.getNumber())) / (bottomSnapshot.getNumber() - topSnapshot.getNumber())) + topHighestTempPoint.getY();
                int highestTempAreaCode = snapshot.getAreaAverageCode(xHighestTempPoint, yHighestTempPoint, Calibration.AREA_THRESHOLD);
                if (lowestTempAreaCode > maxLowestTempAreaCode) {
                    maxLowestTempAreaCode = lowestTempAreaCode;
                }
                if (highestTempAreaCode < minHighestTempAreaCode) {
                    minHighestTempAreaCode = highestTempAreaCode;
                }
                snapshotIdReferencePointsMap.put(
                        snapshot.getNumber(),
                        new ImmutablePair<>(
                                new Point2D(xLowestTempPoint, yLowestTempPoint),
                                new Point2D(xHighestTempPoint, yHighestTempPoint)
                        )
                );

            }

            Calibration calibration = new Calibration(
                    new ImmutablePair(
                            topSnapshot.getNumber(),
                            bottomSnapshot.getNumber()
                    ),
                    snapshotIdReferencePointsMap,
                    new ImmutablePair<>(
                            topSnapshot.getAreaAverageCode(topLowestTempPoint, Calibration.AREA_THRESHOLD),
                            topSnapshot.getAreaAverageCode(topHighestTempPoint, Calibration.AREA_THRESHOLD)
                    ),
                    new ImmutablePair<>(
                            bottomSnapshot.getAreaAverageCode(bottomLowestTempPoint, Calibration.AREA_THRESHOLD),
                            bottomSnapshot.getAreaAverageCode(bottomHighestTempPoint, Calibration.AREA_THRESHOLD)
                    ),
                    maxLowestTempAreaCode,
                    minHighestTempAreaCode
            );
            calibrations.add(calibration);
            listViewCalibrations.getItems().add(calibration);
            System.out.println();

            calibrator.calibrate(snapshots, calibration);

            drawer.drawReferenceSnapshot(canvasTopGc, topReferenceSnapshot);
            drawer.drawReferenceSnapshot(canvasBottomGc, bottomReferenceSnapshot);

            if (calibrations.size() > 1) {
                for (int i = calibrations.size() - 2; i >= 0; i--) { // From penultimate to first
                    Calibration previousCalibration = calibrations.get(i);
                    Snapshot snapshot = snapshots.get(previousCalibration.getSnapshotIdsRange().getRight());
                    Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap12345 = previousCalibration.getSnapshotIdReferencePointsMap();
                    int lowestCode = snapshot.getAreaAverageCode(snapshotIdReferencePointsMap12345.get(snapshot.getNumber()).getLeft(), Calibration.AREA_THRESHOLD);
                    int highestCode = snapshot.getAreaAverageCode(snapshotIdReferencePointsMap12345.get(snapshot.getNumber()).getRight(), Calibration.AREA_THRESHOLD);
                    if (lowestCode > previousCalibration.getTargetLowestTempAreaCode()) {
                        previousCalibration.setTargetLowestTempAreaCode(lowestCode);
                    }
                    if (highestCode < previousCalibration.getTargetHighestTempAreaCode()) {
                        previousCalibration.setTargetHighestTempAreaCode(highestCode);
                    }

                    calibrator.calibrate(snapshots, previousCalibration);
                  }
            }
        }
    }

    @FXML
    public void canvasTopOnMouseClicked(MouseEvent event) {
        if (event.isShiftDown() && Objects.nonNull(topReferenceSnapshot)) {
            Point2D point = new Point2D(event.getX(), event.getY());
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                topReferenceSnapshot.setLowestTempPoint(point);
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                topReferenceSnapshot.setHighestTempPoint(point);
            }
            drawer.drawReferenceSnapshot(canvasTopGc, topReferenceSnapshot);
        }
    }

    @FXML
    public void canvasBottomOnMouseClicked(MouseEvent event) {
        if (event.isShiftDown() && Objects.nonNull(bottomReferenceSnapshot)) {
            Point2D point = new Point2D(event.getX(), event.getY());
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                bottomReferenceSnapshot.setLowestTempPoint(point);
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                bottomReferenceSnapshot.setHighestTempPoint(point);
            }
            drawer.drawReferenceSnapshot(canvasBottomGc, bottomReferenceSnapshot);
        }
    }

    @FXML
    public void listViewSnapshotsOnMouseClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (isDoubleClick(event)) {
                if (CollectionUtils.isNotEmpty(listViewSnapshots.getItems())) {
                    Dialog<ReferenceSnapshotType> dialog = new ChoiceDialog<>(
                            ReferenceSnapshotType.BOTTOM,
                            ReferenceSnapshotType.TOP,
                            ReferenceSnapshotType.BOTTOM
                    );
                    dialog.setTitle("Установить");
                    dialog.setHeaderText("Выберите какой снимок\r\nнеобходимо обновить");
                    Optional<ReferenceSnapshotType> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        Snapshot selectedSnapshot = listViewSnapshots.getSelectionModel().getSelectedItem();
                        if (ReferenceSnapshotType.TOP.equals(result.get())) {
                            selectTopReferenceSnapshot(selectedSnapshot);
                        } else {
                            selectBottomReferenceSnapshot(selectedSnapshot);
                        }
                    }
                }
            }
        }

    }

    @FXML
    public void canvasSpectrumOnMouseMoved(MouseEvent event) {
        updateStatusBar(
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                String.valueOf(
                        ColorUtils.getCode((int) event.getX())
                )
        );
    }

    @FXML
    public void canvasSpectrumOnMouseExited() {
        updateStatusBar(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @FXML
    public void canvasTopOnMouseMoved(MouseEvent event) {
        if (Objects.nonNull(topReferenceSnapshot)) {
            updateStatusBar(
                    String.valueOf(
                            event.getX()
                    ),
                    String.valueOf(
                            event.getY()
                    ),
                    String.valueOf(
                            topReferenceSnapshot.getSnapshot().getCode(event.getX(), event.getY())
                    )
            );
        }
    }

    @FXML
    public void canvasTopOnMouseExited() {
        updateStatusBar(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @FXML
    public void canvasBottomOnMouseMoved(MouseEvent event) {
        if (Objects.nonNull(bottomReferenceSnapshot)) {
            updateStatusBar(
                    String.valueOf(
                            event.getX()
                    ),
                    String.valueOf(
                            event.getY()
                    ),
                    String.valueOf(
                            bottomReferenceSnapshot.getSnapshot().getCode(event.getX(), event.getY())
                    )
            );
        }
    }

    @FXML
    public void canvasBottomOnMouseExited() {
        updateStatusBar(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    private void drawSpectrum() {
        canvasSpectrumGc.setLineWidth(1.0);
        for (int x = 0; x < canvasSpectrum.getWidth(); x++) {
            canvasSpectrumGc.setStroke(ColorUtils.getColor(x));
            canvasSpectrumGc.strokeLine(x, 0, x, canvasSpectrum.getHeight());
        }
    }

    private void initializeStatusBar() {
        labelX = new Label();
        labelY = new Label();
        labelCode = new Label();
        updateStatusBar(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
        statusBar.getRightItems()
                .addAll(
                        labelX,
                        new Label(STATUS_BAR_ELEMENT_SEPARATOR),
                        labelY,
                        new Label(STATUS_BAR_ELEMENT_SEPARATOR),
                        labelCode
                );
    }

    private void updateStatusBar(String xValue, String yValue, String codeValue) {
        labelX.setText(String.format(STATUS_BAR_X_TEMPLATE, xValue));
        labelY.setText(String.format(STATUS_BAR_Y_TEMPLATE, yValue));
        labelCode.setText(String.format(STATUS_BAR_CODE_TEMPLATE, codeValue));
    }

    private Task createFilesPreprocessing(List<File> files) {
        return new Task() {
            @Override
            protected Object call() {
                for (File file : files) {
                    if (!this.isCancelled()) {
                        Snapshot snapshot = parseSnapshot(file, snapshots.size() + 1);
                        snapshots.put(snapshot.getNumber(), snapshot);
                        updateMessage(String.format("%s из %s", snapshots.size(), files.size()));
                        updateProgress(snapshots.size(), files.size());
                    }
                }
                return null;
            }
        };
    }

    private Snapshot parseSnapshot(File file, int fileNumber) { //TODO: refactoring and extract
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] b = new byte[4];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(b);
            int width = ((b[0] & 0xff)) | ((b[1] & 0xff) << 8);
            int height = ((b[2] & 0xff)) | ((b[3] & 0xff) << 8);

            int[][] codesMap = new int[height][width];

            for (int y = 0; y < height; y++) {
                byte[] line = new byte[width * 2];
                //noinspection ResultOfMethodCallIgnored
                inputStream.read(line);
                for (int x = 0; x < width; x++) {
                    codesMap[y][x] = ((line[x * 2] & 0xff)) | ((line[x * 2 + 1] & 0xff) << 8);
                }
            }
            return new Snapshot(fileNumber, file.getName(), codesMap);
        } catch (IOException e) {
            throw new SnapshotParsingException("Error snapshot parsing", e);
        }
    }

    private void clearCanvases() {
        drawer.fillCanvas(canvasTopGc, canvasTop.getWidth(), canvasTop.getHeight(), Color.WHITESMOKE);
        drawer.fillCanvas(canvasBottomGc, canvasBottom.getWidth(), canvasBottom.getHeight(), Color.WHITESMOKE);
        labelTopCanvas.setText(StringUtils.EMPTY);
        labelBottomCanvas.setText(StringUtils.EMPTY);
    }

    private void selectTopReferenceSnapshot(Snapshot snapshot) {
        topReferenceSnapshot = new ReferenceSnapshot(snapshot);
        drawer.drawReferenceSnapshot(canvasTopGc, topReferenceSnapshot);
        labelTopCanvas.setText(snapshot.toString());
    }

    private void selectBottomReferenceSnapshot(Snapshot snapshot) {
        bottomReferenceSnapshot = new ReferenceSnapshot(snapshot);
        drawer.drawReferenceSnapshot(canvasBottomGc, bottomReferenceSnapshot);
        labelBottomCanvas.setText(snapshot.toString());
    }

    private ChangeListener<Calibration> getCalibrationChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (Objects.nonNull(newValue)) {
                Integer topSnapshotId = newValue.getSnapshotIdsRange().getLeft();
                Integer bottomSnapshotId = newValue.getSnapshotIdsRange().getRight();

                labelRangeValue.setText(String.format("[%05d] - [%05d]", topSnapshotId, bottomSnapshotId));

                Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap = newValue.getSnapshotIdReferencePointsMap();
                Pair<Point2D, Point2D> topSnapshotReferencePoints = snapshotIdReferencePointsMap.get(topSnapshotId);
                Pair<Point2D, Point2D> bottomSnapshotReferencePoints = snapshotIdReferencePointsMap.get(bottomSnapshotId);
                Pair<Integer, Integer> originalCodesOnTopSnapshot = newValue.getOriginalCodesOnTopSnapshot();
                Pair<Integer, Integer> originalCodesOnBottomSnapshot = newValue.getOriginalCodesOnBottomSnapshot();

                labelXTopLowestAreaValue.setText(String.valueOf(topSnapshotReferencePoints.getLeft().getX()));
                labelYTopLowestAreaValue.setText(String.valueOf(topSnapshotReferencePoints.getLeft().getY()));
                labelOriginalCodeTopLowestAreaValue.setText(String.valueOf(originalCodesOnTopSnapshot.getLeft()));
                labelXBottomLowestAreaValue.setText(String.valueOf(bottomSnapshotReferencePoints.getLeft().getX()));
                labelYBottomLowestAreaValue.setText(String.valueOf(bottomSnapshotReferencePoints.getLeft().getY()));
                labelOriginalCodeBottomLowestAreaValue.setText(String.valueOf(originalCodesOnBottomSnapshot.getLeft()));
                labelTargetCodeLowestTempAreaValue.setText(String.valueOf(newValue.getTargetLowestTempAreaCode()));

                labelXTopHighestAreaValue.setText(String.valueOf(topSnapshotReferencePoints.getRight().getX()));
                labelYTopHighestAreaValue.setText(String.valueOf(topSnapshotReferencePoints.getRight().getY()));
                labelOriginalCodeTopHighestAreaValue.setText(String.valueOf(originalCodesOnTopSnapshot.getRight()));
                labelXBottomHighestAreaValue.setText(String.valueOf(bottomSnapshotReferencePoints.getRight().getX()));
                labelYBottomHighestAreaValue.setText(String.valueOf(bottomSnapshotReferencePoints.getRight().getY()));
                labelOriginalCodeBottomHighestAreaValue.setText(String.valueOf(originalCodesOnBottomSnapshot.getRight()));
                labelTargetCodeHighestTempAreaValue.setText(String.valueOf(newValue.getTargetHighestTempAreaCode()));
            }
        };
    }

    private boolean isDoubleClick(MouseEvent event) {
        return event.getClickCount() == 2;
    }

    private void clearLabels() {
        labelTopCanvas.setText(StringUtils.EMPTY);
        labelBottomCanvas.setText(StringUtils.EMPTY);
        labelRangeValue.setText(StringUtils.EMPTY);
        labelXTopLowestAreaValue.setText(StringUtils.EMPTY);
        labelYTopLowestAreaValue.setText(StringUtils.EMPTY);
        labelOriginalCodeTopLowestAreaValue.setText(StringUtils.EMPTY);
        labelXBottomLowestAreaValue.setText(StringUtils.EMPTY);
        labelYBottomLowestAreaValue.setText(StringUtils.EMPTY);
        labelOriginalCodeBottomLowestAreaValue.setText(StringUtils.EMPTY);
        labelTargetCodeLowestTempAreaValue.setText(StringUtils.EMPTY);
        labelXTopHighestAreaValue.setText(StringUtils.EMPTY);
        labelYTopHighestAreaValue.setText(StringUtils.EMPTY);
        labelOriginalCodeTopHighestAreaValue.setText(StringUtils.EMPTY);
        labelXBottomHighestAreaValue.setText(StringUtils.EMPTY);
        labelYBottomHighestAreaValue.setText(StringUtils.EMPTY);
        labelOriginalCodeBottomHighestAreaValue.setText(StringUtils.EMPTY);
        labelTargetCodeHighestTempAreaValue.setText(StringUtils.EMPTY);
    }

    enum WorkStation {
        HOME,
        LAPTOP,
        WORK
    }
}
