package ru.komarov.university.calibrator.domain;

import javafx.geometry.Point2D;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * <p>
 * Created on 5/24/2019.
 *
 * @author Vasilii Komarov
 */
@Getter
public class Snapshot {
    private final int number;
    private final String name;
    private final int[][] codesMap;
    private final int width;
    private final int height;

    public Snapshot(int number, String name, int[][] codesMap) {
        this.number = number;
        this.name = name;
        this.codesMap = codesMap;
        if (ArrayUtils.isEmpty(codesMap)) {
            throw new RuntimeException("Error snapshot initialization - map of pixels map is empty");
        }
        this.width = codesMap[0].length;
        this.height = codesMap.length;
    }

    public int getCode(int x, int y) {
        return codesMap[y][x];
    }

    public int getCode(double x, double y) {
        return getCode((int) x, (int) y);
    }

    public int getAreaAverageCode(int centerX, int centerY, int threshold) {
        int leftX = centerX - threshold < 0 ? 0 : centerX - threshold;
        int rightX = centerX + threshold > width - 1 ? width - 1 : centerX + threshold;
        int topY = centerY - threshold < 0 ? 0 : centerY - threshold;
        int bottomY = centerY + threshold > height - 1 ? height - 1 : centerY + threshold;
        int numberOfPoints = 0;
        int sumOfCodes = 0;
        for (int x = leftX; x <= rightX; x++) {
            for (int y = topY; y <= bottomY ; y++) {
                sumOfCodes += getCode(x, y);
                numberOfPoints++;
            }
        }
        return sumOfCodes / numberOfPoints;
    }

    public int getAreaAverageCode(double centerX, double centerY, int threshold) {
        return getAreaAverageCode((int) centerX, (int) centerY, threshold);
    }

    public int getAreaAverageCode(Point2D center, int threshold) {
        return getAreaAverageCode(center.getX(), center.getY(), threshold);
    }

    @Override
    public String toString() {
        return String.format("[%05d] - %s", this.number, this.name);
    }
}
