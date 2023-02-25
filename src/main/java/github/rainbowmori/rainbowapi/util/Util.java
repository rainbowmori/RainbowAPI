package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RMHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * よく使うメゾットがある
 */

public class Util {

    public static final PrefixUtil util = RMHome.getRainbowAPI().prefixUtil;

    public static Component mm(Object str) {
        return IsObjectUtil.IsComponent(str) ? ((Component) str) : MiniMessage.miniMessage().deserialize(vanillaToMM(String.valueOf(str)));
    }
    
    public static void consoleCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
    
    public static void executeCommand(Player execute, String command) {
        if (execute.isOp()) {
            execute.performCommand(command);
        } else {
            try {
                execute.setOp(true);
                execute.performCommand(command);
            } finally {
                execute.setOp(false);
            }
        }
    }

    public static String vanillaToMM(String str) {
        if (str.contains("&0")) str = str.replace("&0", "<black>");
        if (str.contains("&1")) str = str.replace("&1", "<dark_blue>");
        if (str.contains("&2")) str = str.replace("&2", "<dark_green>");
        if (str.contains("&3")) str = str.replace("&3", "<dark_aqua>");
        if (str.contains("&4")) str = str.replace("&4", "<dark_red>");
        if (str.contains("&5")) str = str.replace("&5", "<dark_purple>");
        if (str.contains("&6")) str = str.replace("&6", "<gold>");
        if (str.contains("&7")) str = str.replace("&7", "<gray>");
        if (str.contains("&8")) str = str.replace("&8", "<dark_gray>");
        if (str.contains("&9")) str = str.replace("&9", "<blue>");
        if (str.contains("&a")) str = str.replace("&a", "<green>");
        if (str.contains("&b")) str = str.replace("&b", "<aqua>");
        if (str.contains("&c")) str = str.replace("&c", "<red>");
        if (str.contains("&d")) str = str.replace("&d", "<light_purple>");
        if (str.contains("&e")) str = str.replace("&e", "<yellow>");
        if (str.contains("&f")) str = str.replace("&f", "<white>");
        if (str.contains("&g")) str = str.replace("&g", "<minecoin_gold>");
        if (str.contains("&k")) str = str.replace("&k", "<obfuscated>");
        if (str.contains("&l")) str = str.replace("&l", "<bold>");
        if (str.contains("&m")) str = str.replace("&m", "<strikethrough>");
        if (str.contains("&n")) str = str.replace("&n", "<underlined>");
        if (str.contains("&o")) str = str.replace("&o", "<italic>");
        if (str.contains("&r")) str = str.replace("&r", "<reset>");
        return str;
    }

    /**
     * listの内容をComponentに変換
     *
     * @param list 変換したいlist
     * @return List<Component>
     */

    public static List<Component> ListMM(List<?> list) {
        return list.stream().map(Util::mm).collect(Collectors.toList());
    }

    /**
     * {@link Component}をStringに変換
     *
     * @param str 変換したいコンポーネント
     * @return 変換されたString
     */

    public static String serialize(Component str) {
        return MiniMessage.miniMessage().serialize(str);
    }

    /**
     * show title
     *
     * @param uuid     player uuid
     * @param title    title string
     * @param subtitle subtitle string
     */

    public static void title(UUID uuid, Object title, Object subtitle) {
        title(Bukkit.getPlayer(uuid), title, subtitle);
    }

    /**
     * show title
     *
     * @param player   player
     * @param title    title string
     * @param subtitle subtitle string
     */

    public static void title(Player player, Object title, Object subtitle) {
        if (player != null && player.isOnline()) {
            player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
                Util.mm(Objects.requireNonNullElse(subtitle, ""))));
        }
    }
    
    public static void title(Player player, Object title, Object subtitle,int seconds) {
        if (player != null && player.isOnline()) {
            player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
                Util.mm(Objects.requireNonNullElse(subtitle, "")),Title.Times.times(Duration.ZERO, Duration.ofSeconds(seconds), Duration.ZERO)));
        }
    }

    /**
     * BroadCast
     *
     * @param str message
     */

    public static void Cast(Object str) {
        Bukkit.broadcast(Util.mm(str));
    }
}
