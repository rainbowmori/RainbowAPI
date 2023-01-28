package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Util {

    public static final String RM_Prefix = "<gray>[<red>RM<gray>] ";

    public static void sendRM(UUID uuid, Object str) {
        sendRM(Bukkit.getPlayer(uuid), str);
    }

    public static void sendRM(Player player, Object str) {
        if (player != null) player.sendMessage(RM_Prefix + str);
    }

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
