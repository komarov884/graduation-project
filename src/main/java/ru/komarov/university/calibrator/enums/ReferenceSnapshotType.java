package ru.komarov.university.calibrator.enums;

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
public enum ReferenceSnapshotType {
    TOP("Начальный снимок"),
    BOTTOM("Конечный снимок");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
