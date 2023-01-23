package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static Component mm(Object str) {
        return IsObjectUtil.IsComponent(str) ? ((Component) str) : MiniMessage.miniMessage().deserialize(String.valueOf(str));
    }

    public static List<Component> ListMM(List<?> list) {
        return list.stream().map(Util::mm).collect(Collectors.toList());
    }

    public static String serialize(Component str) {
        return MiniMessage.miniMessage().serialize(str);
    }
}
