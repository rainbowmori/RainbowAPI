package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;

import java.util.List;

public class IsObjectUtil {
    public static boolean IsComponent(Object is) {
        return is instanceof Component;
    }

    public static int IsInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static float IsFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static double IsDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static boolean IsBoolean(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    public static boolean IsListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
