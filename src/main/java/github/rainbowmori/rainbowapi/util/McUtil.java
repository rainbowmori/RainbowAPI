package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RainbowAPI;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class McUtil {
    private final RainbowAPI api;
    private final Logger log = Logger.getLogger("[RMLOG]");

    /**
     * マイクラに関するUtil
     * @param api {@link RainbowAPI}
     */

    public McUtil(RainbowAPI api) {
        this.api = api;
    }

    /**
     * consoleに表示
     * @param object 表示する内容
     */

    public void log(Object object) {
        api.plugin.getLogger().log(Level.INFO,object.toString());
    }

    /**
     * プレイヤーにメッセージ送信
     * @param uuid player uuid
     * @param str message

     */

    public void send(UUID uuid, Object str) {
        send(Bukkit.getPlayer(uuid), str);
    }

    /**
     * プレイヤーにメッセージ送信
     * @param player player
     * @param str message
     */

    public void send(Player player, Object str) {
        if (player != null) player.sendMessage(Util.mm(api.prefix + str));
    }

    /**
     * show title
     * @param uuid player uuid
     * @param title title string
     * @param subtitle subtitle string
     */

    public void title(UUID uuid, Object title, Object subtitle) {
        title(Bukkit.getPlayer(uuid), title, subtitle);
    }

    /**
     * show title
     * @param player player
     * @param title title string
     * @param subtitle subtitle string
     */

    public void title(Player player, Object title, Object subtitle) {
        if (player != null && player.isOnline())
            player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
                    Util.mm(Objects.requireNonNullElse(subtitle, ""))));
    }

    /**
     * BroadCast
     * @param str message
     */

    public void Cast(Object str) {
        Bukkit.broadcast(Util.mm(api.prefix + str));
    }

    /**
     * コマンドをコンソールセンダーで実行
     * @param command command
     */

    public void consoleCommand(String command) {
        api.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * コマンドをプレイヤーで実行
     * @param execute player
     * @param command command
     */

    public void executeCommand(Player execute, String command) {
        if (execute.isOp()) execute.performCommand(command);
        else {
            try {
                execute.setOp(true);
                execute.performCommand(command);
            } finally {
                execute.setOp(false);
            }
        }
    }
}
