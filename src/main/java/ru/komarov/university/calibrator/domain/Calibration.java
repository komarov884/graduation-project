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
    public static final int AREA_THRESHOLD = 2; //TODO: move to another class???

    private final Pair<Integer, Integer> snapshotIdsRange;
    private final Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap;

    private final Pair<Integer, Integer> originalCodesOnTopSnapshot;
    private final Pair<Integer, Integer> originalCodesOnBottomSnapshot;

    @Setter
    private Integer targetLowestTempAreaCode;
    @Setter
    private Integer targetHighestTempAreaCode;

    public Calibration(Pair<Integer, Integer> snapshotIdsRange,
                       Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap,
                       Pair<Integer, Integer> originalCodesOnTopSnapshot,
                       Pair<Integer, Integer> originalCodesOnBottomSnapshot,
                       Integer targetLowestTempAreaCode,
                       Integer targetHighestTempAreaCode) {
        this.snapshotIdsRange = snapshotIdsRange;
        this.snapshotIdReferencePointsMap = snapshotIdReferencePointsMap;
        this.originalCodesOnTopSnapshot = originalCodesOnTopSnapshot;
        this.originalCodesOnBottomSnapshot = originalCodesOnBottomSnapshot;
        this.targetLowestTempAreaCode = targetLowestTempAreaCode;
        this.targetHighestTempAreaCode = targetHighestTempAreaCode;
    }

    @Override
    public String toString() {
        return String.format(
                "Калибровка между [%05d] и [%05d] снимками",
                snapshotIdsRange.getLeft(),
                snapshotIdsRange.getRight()
        );
    }
}
