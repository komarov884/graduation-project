package ru.komarov.university.calibrator.calibrator;

import ru.komarov.university.calibrator.domain.Calibration;
import ru.komarov.university.calibrator.domain.Snapshot;

import java.util.Map;

/**
 * <p>
 * Created on 5/27/2019.
 *
 * @author Vasilii Komarov
 */
public interface Calibrator {
    void calibrate(Map<Integer, Snapshot> snapshots, Calibration calibration);
}
