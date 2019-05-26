package ru.komarov.university.calibrator.domain;

import javafx.geometry.Point2D;
import lombok.Getter;

/**
 * <p>
 * Created on 5/25/2019.
 *
 * @author Vasilii Komarov
 */
@Getter
public class Calibration {
    public static final int AREA_THRESHOLD = 2;

    private final ReferenceSnapshot topReferenceSnapshot;
    private final ReferenceSnapshot bottomReferenceSnapshot;

    private final int originalLowestTempAreaCodeOnTopReferenceSnapshot;
    private final int originalHighestTempAreaCodeOnTopReferenceSnapshot;

    private final int originalLowestTempAreaCodeOnBottomReferenceSnapshot;
    private final int originalHighestTempAreaCodeOnBottomReferenceSnapshot;

    private final int maxLowestTempAreaCode;
    private final int minHighestTempAreaCode;

    public Calibration(ReferenceSnapshot topReferenceSnapshot,
                       ReferenceSnapshot bottomReferenceSnapshot,
                       int maxLowestTempAreaCode,
                       int minHighestTempAreaCode) {
        this.topReferenceSnapshot = topReferenceSnapshot;
        this.bottomReferenceSnapshot = bottomReferenceSnapshot;
        Snapshot topSnapshot = topReferenceSnapshot.getSnapshot();
        Point2D topLowestTempPoint = topReferenceSnapshot.getLowestTempPoint();
        Point2D topHighestTempPoint = topReferenceSnapshot.getHighestTempPoint();
        this.originalLowestTempAreaCodeOnTopReferenceSnapshot = topSnapshot.getAreaAverageCode(
                topLowestTempPoint.getX(),
                topLowestTempPoint.getY(),
                AREA_THRESHOLD
        );
        this.originalHighestTempAreaCodeOnTopReferenceSnapshot = topSnapshot.getAreaAverageCode(
                topHighestTempPoint.getX(),
                topHighestTempPoint.getY(),
                AREA_THRESHOLD
        );
        Snapshot bottomSnapshot = bottomReferenceSnapshot.getSnapshot();
        Point2D bottomLowestTempPoint = bottomReferenceSnapshot.getLowestTempPoint();
        Point2D bottomHighestTempPoint = bottomReferenceSnapshot.getHighestTempPoint();
        this.originalLowestTempAreaCodeOnBottomReferenceSnapshot = bottomSnapshot.getAreaAverageCode(
                bottomLowestTempPoint.getX(),
                bottomLowestTempPoint.getY(),
                AREA_THRESHOLD
        );
        this.originalHighestTempAreaCodeOnBottomReferenceSnapshot = bottomSnapshot.getAreaAverageCode(
                bottomHighestTempPoint.getX(),
                bottomHighestTempPoint.getY(),
                AREA_THRESHOLD
        );
        this.maxLowestTempAreaCode = maxLowestTempAreaCode;
        this.minHighestTempAreaCode = minHighestTempAreaCode;
    }

    @Override
    public String toString() {
        return String.format(
                "Калибровка между [%05d] и [%05d] снимками",
                topReferenceSnapshot.getSnapshot().getNumber(),
                bottomReferenceSnapshot.getSnapshot().getNumber()
        );
    }
}
