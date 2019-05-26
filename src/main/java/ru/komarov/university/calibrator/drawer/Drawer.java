package ru.komarov.university.calibrator.drawer;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.komarov.university.calibrator.domain.ReferenceSnapshot;
import ru.komarov.university.calibrator.domain.Snapshot;
import ru.komarov.university.calibrator.util.ColorUtils;

import java.util.Objects;

/**
 * <p>
 * Created on 5/24/2019.
 *
 * @author Vasilii Komarov
 */
public class Drawer {

    private static final double SELECTED_AREA_INDENT = 2.5;

    public void drawSnapshot(GraphicsContext gc, Snapshot snapshot) {
        for (int i = 0; i < snapshot.getHeight(); i++) {
            for (int j = 0; j < snapshot.getWidth(); j++) {
                int code = snapshot.getCode(j, i);
                gc.setFill(ColorUtils.convertCodeToColor(code));
                gc.fillRect(j, i, 1, 1);
            }
        }
    }

    public void drawReferenceSnapshot(GraphicsContext gc, ReferenceSnapshot referenceSnapshot) {
        drawSnapshot(gc, referenceSnapshot.getSnapshot());
        Point2D highestTempPoint = referenceSnapshot.getHighestTempPoint();
        if (Objects.nonNull(highestTempPoint)) {
            selectArea(gc, highestTempPoint, ColorUtils.getColor(ColorUtils.MIN_COLOR_INDEX));
        }
        Point2D lowestTempPoint = referenceSnapshot.getLowestTempPoint();
        if (Objects.nonNull(lowestTempPoint)) {
            selectArea(gc, lowestTempPoint, ColorUtils.getColor(ColorUtils.MAX_COLOR_INDEX));
        }
    }

    public void selectArea(GraphicsContext gc, Point2D point, Color color) {
        gc.setStroke(color);
        gc.strokeLine(point.getX() - SELECTED_AREA_INDENT, point.getY() - SELECTED_AREA_INDENT,
                point.getX() - SELECTED_AREA_INDENT, point.getY() + SELECTED_AREA_INDENT);
        gc.strokeLine(point.getX() + SELECTED_AREA_INDENT, point.getY() - SELECTED_AREA_INDENT,
                point.getX() + SELECTED_AREA_INDENT, point.getY() + SELECTED_AREA_INDENT);
        gc.strokeLine(point.getX() - SELECTED_AREA_INDENT, point.getY() - SELECTED_AREA_INDENT,
                point.getX() + SELECTED_AREA_INDENT, point.getY() - SELECTED_AREA_INDENT);
        gc.strokeLine(point.getX() - SELECTED_AREA_INDENT, point.getY() + SELECTED_AREA_INDENT,
                point.getX() + SELECTED_AREA_INDENT, point.getY() + SELECTED_AREA_INDENT);
    }

    public void fillCanvas(GraphicsContext gc, double width, double height, Color color) {
        gc.setFill(color);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gc.fillRect(x, y, 1, 1);
            }
        }
    }
}
