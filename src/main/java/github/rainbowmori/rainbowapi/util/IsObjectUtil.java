package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;

public class IsObjectUtil {
    public static boolean IsComponent(Object is) {
        return is instanceof Component;
    }

    public static Optional<Integer> IsInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    public static Optional<Float> IsFloat(String s) {
        try {
            return Optional.of(Float.parseFloat(s));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    public static Optional<Double> IsDouble(String s) {
        try {
            return Optional.of(Double.parseDouble(s));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    public static boolean IsBoolean(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    public static boolean IsListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
