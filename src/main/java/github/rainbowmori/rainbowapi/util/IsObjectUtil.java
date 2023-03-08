package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;

import java.util.List;

public class IsObjectUtil {
    public static boolean IsComponent(Object is) {
        return is instanceof Component;
    }
    
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }
    
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static boolean IsListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
