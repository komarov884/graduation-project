package ru.komarov.university.calibrator.util;

import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Created on 5/24/2019.
 *
 * @author Vasilii Komarov
 */
@UtilityClass
public class ColorUtils { //todo rename

    public static final int NUMBER_OF_CODES = 16384;
    public static final int MIN_CODE = 0;
    public static final int MAX_CODE = NUMBER_OF_CODES - 1;

    public static final int NUMBER_OF_COLORS = 1276;
    public static final int MIN_COLOR_INDEX = 0;
    public static final int MAX_COLOR_INDEX = NUMBER_OF_COLORS - 1;

    private static final double SCALABILITY_FACTOR = (double) MAX_CODE / (double) MAX_COLOR_INDEX;

    private static final int[] CODE_COLOR_INDEX_MATCHING = new int[NUMBER_OF_CODES];
    private static final Map<Integer, Color> COLORS = new HashMap<>(NUMBER_OF_COLORS);

    static {
        fillCodeColorIndexMatching();
        fillColors();
    }

    private static void fillCodeColorIndexMatching() {
        for (int i = 0; i < NUMBER_OF_CODES; i++) {
            CODE_COLOR_INDEX_MATCHING[i] = (int) (i / SCALABILITY_FACTOR);
        }
    }

    private static void fillColors() {
        int colorIndex = 0;

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int i = 0; i < 255; i++) {
            COLORS.put(colorIndex, Color.rgb(red, green, blue));
            blue++;
            colorIndex++;
        }

        for (int i = 0; i < 255; i++) {
            COLORS.put(colorIndex, Color.rgb(red, green, blue));
            green++;
            colorIndex++;
        }

        for (int i = 0; i < 255; i++) {
            COLORS.put(colorIndex, Color.rgb(red, green, blue));
            blue--;
            colorIndex++;
        }

        for (int i = 0; i < 255; i++) {
            COLORS.put(colorIndex, Color.rgb(red, green, blue));
            red++;
            colorIndex++;
        }

        for (int i = 0; i < 255; i++) {
            COLORS.put(colorIndex, Color.rgb(red, green, blue));
            green--;
            colorIndex++;
        }

        COLORS.put(colorIndex, Color.rgb(red, green, blue));
    }

    public static Color getColor(int colorIndex) {
        return COLORS.get(colorIndex);
    }

    public static Color convertCodeToColor(int code) {
        int colorIndex = CODE_COLOR_INDEX_MATCHING[code];
        return COLORS.get(colorIndex);
    }

    public static int getCode(int colorIndex) {
        return (int) (colorIndex * SCALABILITY_FACTOR);
    }
}
