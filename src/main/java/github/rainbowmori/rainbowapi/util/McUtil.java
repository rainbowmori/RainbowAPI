package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

public class McUtil {
    private final RainbowAPI api;
    private final Logger log;

    /**
     * マイクラに関するUtil
     *
     * @param api {@link RainbowAPI}
     */

    public McUtil(RainbowAPI api) {
        this.api = api;
        this.log = api.plugin.getLogger();
    }

    public void logInfo(String message) {
        log.info(message);
    }

    public void logNormal(String message) {
        log.info(message);
    }

    public void logWarning(String message) {
        log.warning(message);
    }

    public void logError(String message) {
        log.severe(message);
    }

    /**
     * プレイヤーにメッセージ送信
     *
     * @param uuid player uuid
     * @param str  message
     */

    public void send(UUID uuid, Object str) {
        send(Bukkit.getPlayer(uuid), str);
    }

    /**
     * プレイヤーにメッセージ送信
     *
     * @param player player
     * @param str    message
     */

    public void send(Player player, Object str) {
        if (player != null) {
            player.sendMessage(Util.mm(api.prefix + str));
        }
    }

    /**
     * BroadCast
     *
     * @param str message
     */

    public void Cast(Object str) {
        Bukkit.broadcast(Util.mm(api.prefix + str));
    }

    /**
     * コマンドをコンソールセンダーで実行
     *
     * @param command command
     */

    public void consoleCommand(String command) {
        api.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * コマンドをプレイヤーで実行
     *
     * @param execute player
     * @param command command
     */

    public void executeCommand(Player execute, String command) {
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
}
