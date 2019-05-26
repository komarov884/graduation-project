package ru.komarov.university.calibrator.domain;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Created on 5/24/2019.
 *
 * @author Vasilii Komarov
 */
@Getter
public class ReferenceSnapshot {

    private final Snapshot snapshot;

    @Setter
    private Point2D lowestTempPoint;
    @Setter
    private Point2D highestTempPoint;

    public ReferenceSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }
}
