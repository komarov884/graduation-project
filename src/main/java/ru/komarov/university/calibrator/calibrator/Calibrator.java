package ru.komarov.university.calibrator.calibrator;

import ru.komarov.university.calibrator.domain.Calibration;
import ru.komarov.university.calibrator.domain.Snapshot;

import java.util.List;

/**
 * <p>
 * Created on 5/27/2019.
 *
 * @author Vasilii Komarov
 */
public interface Calibrator {
    void calibrate(List<Snapshot> snapshots, Calibration calibration);
}
