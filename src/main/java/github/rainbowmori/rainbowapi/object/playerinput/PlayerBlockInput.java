package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PlayerBlockInput {
    public static final Map<Player, PlayerBlockInput> inputMap = new HashMap<>();

    public final Plugin plugin;

    public final String message;

    public final Function<BlockBreakEvent,String> success;
    public final String error;

    public final RainbowAPI rainbowAPI;

    public final BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate;

    public PlayerBlockInput(Player player, Plugin plugin, String message,
                            String success, String error, BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate) {
        this(player, plugin, message, blockBreakEvent -> success, error, predicate);
    }

    public PlayerBlockInput(Player player, Plugin plugin, String message,
                            Function<BlockBreakEvent,String> success, String error, BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate) {
        this.plugin = plugin;
        this.success = success;
        this.message = message;
        this.error = error;
        this.rainbowAPI = RainbowAPI.apis.get(plugin);
        this.predicate = predicate;
        if (inputMap.containsKey(player)) rainbowAPI.mcUtil.send(player, "<red>すでにブロック入力中です");
        else {
            rainbowAPI.mcUtil.send(player, message);
            inputMap.put(player, this);
        }
    }

    public static boolean isInputted(Player player) {
        if (inputMap.containsKey(player)) return false;
        Util.sendRM(player, "<red>現在入力中です");
        return true;
    }

    public void build(BlockBreakEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        if (predicate.test(this, e)) {
            rainbowAPI.mcUtil.send(p, success.apply(e));
            inputMap.remove(p);
        } else {
            rainbowAPI.mcUtil.send(p, "<red>" + error);
            rainbowAPI.mcUtil.send(p, message);
        }
    }
}
