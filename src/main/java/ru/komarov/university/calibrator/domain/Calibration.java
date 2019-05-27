package ru.komarov.university.calibrator.domain;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * <p>
 * Created on 5/25/2019.
 *
 * @author Vasilii Komarov
 */
@Getter
public class Calibration {
    public static final int AREA_THRESHOLD = 2;

    //private final ReferenceSnapshot topReferenceSnapshot;//todo Это не надо. вместо этого сделать набор референсных точкек. айди->точка, точка
    //private final ReferenceSnapshot bottomReferenceSnapshot;

    private final Pair<Integer, Integer> snapshotIdsRange;
    private final Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap;

    //private final int originalLowestTempAreaCodeOnTopReferenceSnapshot;
    //private final int originalHighestTempAreaCodeOnTopReferenceSnapshot;
//
    //private final int originalLowestTempAreaCodeOnBottomReferenceSnapshot;
    //private final int originalHighestTempAreaCodeOnBottomReferenceSnapshot;

    @Setter
    private Integer targetLowestTempAreaCode;
    @Setter
    private Integer targetHighestTempAreaCode;

    public Calibration(Pair<Integer, Integer> snapshotIdsRange,
                       Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap) {
        this.snapshotIdsRange = snapshotIdsRange;
        this.snapshotIdReferencePointsMap = snapshotIdReferencePointsMap;
    }

//public Calibration(ReferenceSnapshot topReferenceSnapshot,
    //                   ReferenceSnapshot bottomReferenceSnapshot,
    //                   int targetLowestTempAreaCode,
    //                   int targetHighestTempAreaCode) {
    //    this.topReferenceSnapshot = topReferenceSnapshot;
    //    this.bottomReferenceSnapshot = bottomReferenceSnapshot;
    //    Snapshot topSnapshot = topReferenceSnapshot.getSnapshot();
    //    Point2D topLowestTempPoint = topReferenceSnapshot.getLowestTempPoint();
    //    Point2D topHighestTempPoint = topReferenceSnapshot.getHighestTempPoint();
    //    this.originalLowestTempAreaCodeOnTopReferenceSnapshot = topSnapshot.getAreaAverageCode(
    //            topLowestTempPoint.getX(),
    //            topLowestTempPoint.getY(),
    //            AREA_THRESHOLD
    //    );
    //    this.originalHighestTempAreaCodeOnTopReferenceSnapshot = topSnapshot.getAreaAverageCode(
    //            topHighestTempPoint.getX(),
    //            topHighestTempPoint.getY(),
    //            AREA_THRESHOLD
    //    );
    //    Snapshot bottomSnapshot = bottomReferenceSnapshot.getSnapshot();
    //    Point2D bottomLowestTempPoint = bottomReferenceSnapshot.getLowestTempPoint();
    //    Point2D bottomHighestTempPoint = bottomReferenceSnapshot.getHighestTempPoint();
    //    this.originalLowestTempAreaCodeOnBottomReferenceSnapshot = bottomSnapshot.getAreaAverageCode(
    //            bottomLowestTempPoint.getX(),
    //            bottomLowestTempPoint.getY(),
    //            AREA_THRESHOLD
    //    );
    //    this.originalHighestTempAreaCodeOnBottomReferenceSnapshot = bottomSnapshot.getAreaAverageCode(
    //            bottomHighestTempPoint.getX(),
    //            bottomHighestTempPoint.getY(),
    //            AREA_THRESHOLD
    //    );
    //    this.targetLowestTempAreaCode = targetLowestTempAreaCode;
    //    this.targetHighestTempAreaCode = targetHighestTempAreaCode;
    //}

    @Override
    public String toString() {
        return String.format(
                "Калибровка между [%05d] и [%05d] снимками",
                snapshotIdsRange.getLeft(),
                snapshotIdsRange.getRight()
        );
    }
}
