package ru.komarov.university.calibrator.calibrator;

import javafx.geometry.Point2D;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.komarov.university.calibrator.domain.Calibration;
import ru.komarov.university.calibrator.domain.Snapshot;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Created on 5/27/2019.
 *
 * @author Vasilii Komarov
 */
public class CalibratorImpl implements Calibrator {
    @Override
    public void calibrate(Map<Integer, Snapshot> snapshots, Calibration calibration) {
        if (Objects.isNull(calibration.getTargetHighestTempAreaCode())
                || Objects.isNull(calibration.getTargetLowestTempAreaCode())) {
            throw new RuntimeException();//todo
        }
        if (MapUtils.isEmpty(snapshots)) {
            throw new RuntimeException();//todo
        }

        Map<Integer, Pair<Point2D, Point2D>> snapshotIdReferencePointsMap
                = calibration.getSnapshotIdReferencePointsMap();
        int targetHighestTempAreaCode = calibration.getTargetHighestTempAreaCode();
        int targetLowestTempAreaCode = calibration.getTargetLowestTempAreaCode();

        for (int j = calibration.getSnapshotIdsRange().getLeft(); j <= calibration.getSnapshotIdsRange().getRight(); j++) {
            Snapshot snapshot = snapshots.get(j);
            int[][] codesMap = snapshot.getCodesMap();

            Pair<Point2D, Point2D> referencePoints = snapshotIdReferencePointsMap.get(j);
            int lowestTempAreaCode = snapshot.getAreaAverageCode(referencePoints.getLeft(), Calibration.AREA_THRESHOLD);//todo это выполняется дважды! может добавить температуру в какую то структуру?!
            int highestTempAreaCode = snapshot.getAreaAverageCode(referencePoints.getRight(), Calibration.AREA_THRESHOLD);

            for (int x = 0; x < snapshot.getWidth(); x++) {
                for (int y = 0; y < snapshot.getHeight(); y++) {
                    int oldCode = codesMap[y][x];
                    codesMap[y][x] = (((targetHighestTempAreaCode - targetLowestTempAreaCode) * (oldCode - lowestTempAreaCode)) / (highestTempAreaCode - lowestTempAreaCode)) + targetLowestTempAreaCode;
                }
            }
        }

    }
}
