package ru.komarov.university.calibrator.calibrator;

import org.apache.commons.collections4.CollectionUtils;
import ru.komarov.university.calibrator.domain.Calibration;
import ru.komarov.university.calibrator.domain.Snapshot;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Created on 5/27/2019.
 *
 * @author Vasilii Komarov
 */
public class CalibratorImpl implements Calibrator {
    @Override
    public void calibrate(List<Snapshot> snapshots, Calibration calibration) {
        if (Objects.isNull(calibration.getTargetHighestTempAreaCode())
                || Objects.isNull(calibration.getTargetLowestTempAreaCode())) {
            throw new RuntimeException();//todo
        }
        if (CollectionUtils.isEmpty(snapshots)) {
            throw new RuntimeException();//todo
        }


    }
}
