package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * よく使うメゾットがある
 */

public class Util {

    public static final String RM_Prefix = "<gray>[<red>RM<gray>] ";

    /**
     * プレイヤーにメッセージを送る
     * @param uuid player uuid
     * @param str message
     */
    public static void sendRM(UUID uuid, Object str) {
        sendRM(Bukkit.getPlayer(uuid), str);
    }

    /**
     * プレイヤーにメッセージを送る
     * @param player player
     * @param str message
     */
    public static void sendRM(Player player, Object str) {
        if (player != null) player.sendMessage(RM_Prefix + str);
    }

    /**
     * オブジェクトをComponentに変更する
     * @param str 変更するオブジェクト
     * @return {@link Component}
     */

    public static Component mm(Object str) {
        return IsObjectUtil.IsComponent(str) ? ((Component) str) : MiniMessage.miniMessage().deserialize(String.valueOf(str));
    }

    /**
     * listの内容をComponentに変換
     * @param list 変換したいlist
     * @return List<Component>
     */

    public static List<Component> ListMM(List<?> list) {
        return list.stream().map(Util::mm).collect(Collectors.toList());
    }

    /**
     * {@link Component}をStringに変換
     * @param str 変換したいコンポーネント
     * @return 変換されたString
     */

    public static String serialize(Component str) {
        return MiniMessage.miniMessage().serialize(str);
    }
}
