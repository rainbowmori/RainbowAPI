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

    public McUtil(RainbowAPI api) {
        this.api = api;
    }

    public void log(Object object) {
        log.log(Level.INFO, api.plugin.getName() + " | " + object);
    }

    public void send(UUID uuid, Object str) {
        send(Bukkit.getPlayer(uuid), str);
    }

    public void send(Player player, Object str) {
        if (player != null) player.sendMessage(Util.mm(api.prefix + str));
    }

    public void title(UUID uuid, Object title, Object subtitle) {
        title(Bukkit.getPlayer(uuid), title, subtitle);
    }

    public void title(Player player, Object title, Object subtitle) {
        if (player != null && player.isOnline())
            player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
                    Util.mm(Objects.requireNonNullElse(subtitle, ""))));
    }

    public void Cast(Object str) {
        Bukkit.broadcast(Util.mm(api.prefix + str));
    }

    public void consoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

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
