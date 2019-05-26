package ru.komarov.university.calibrator.domain;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Created on 5/25/2019.
 *
 * @author Vasilii Komarov
 */
@Getter
@RequiredArgsConstructor
public class MarkedSnapshot { //todo RENAME?
    private final Snapshot snapshot;
    //private final Point2D lowestTempPoint;
    //private final Point2D highestTempPoint;
    private final int lowestTempAreaCode;
    private final int highestTempAreaCode;
}
