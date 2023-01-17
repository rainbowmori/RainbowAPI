package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RainbowAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Util {

    private static final Logger log = Logger.getLogger("[RMLOG]");
    public static void log(Object object) {
        log.log(Level.INFO,RainbowAPI.getPlugin().getName() + " | " + object);
    }

    public static void send(UUID uuid, Object str) {
        send(Bukkit.getPlayer(uuid), str);
    }

    public static void send(Player player, Object str) {
        if (player != null) player.sendMessage(Pmm(player, RainbowAPI.getPrefix() + str));
    }

    public static void sender(CommandSender sender, String str) {
        sender.sendMessage(Util.mm(RainbowAPI.getPrefix() + str));
    }

    public static void title(UUID uuid, Object title, Object subtitle) {
        title(Bukkit.getPlayer(uuid), title, subtitle);
    }

    public static void title(Player player, Object title, Object subtitle) {
        if (player != null && player.isOnline())
            player.showTitle(Title.title(Pmm(player, Objects.requireNonNullElse(title, "")),
                    Pmm(player, Objects.requireNonNullElse(subtitle, ""))));
    }

    public static void Cast(Object str) {
        Bukkit.broadcast(mm(RainbowAPI.getPrefix() + str));
    }

    public static Component Pmm(Player player, Object string) {
        return mm(Placeholder(player, String.valueOf(string)));
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

    public static void consoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void consoleCommand(Player player, String command) {
        consoleCommand(Placeholder(player, command));
    }

    public static void executeCommand(Player player, String command) {
        executeCommand(player, command, player);
    }

    public static void executeCommand(Player execute, String command, Player player) {
        if (player.isOp()) execute.performCommand(Placeholder(player, command));
        else {
            try {
                player.setOp(true);
                execute.performCommand(Placeholder(player, command));
            } finally {
                player.setOp(false);
            }
        }
    }

    public static String Placeholder(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static class Send {

        private final Player p;
        public Send(Player player) {
            this.p = player;
        }

        public void send(Object text) {
            Util.send(p,text);
        }
    }

}
