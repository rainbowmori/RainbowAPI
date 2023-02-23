package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.RMHome;
import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PlayerBlockInput {
    public static final Map<UUID, PlayerBlockInput> inputMap = new HashMap<>();

    public final Plugin plugin;

    public final String message;

    public final Function<BlockBreakEvent, String> success;
    public final String error;

    public final RainbowAPI rainbowAPI;

    public final BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate;

    public PlayerBlockInput(Player player, JavaPlugin plugin, String message,
                            String success, String error, BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate) {
        this(player, plugin, message, blockBreakEvent -> success, error, predicate);
    }

    public PlayerBlockInput(Player player, JavaPlugin plugin, String message,
                            Function<BlockBreakEvent, String> success, String error, BiPredicate<PlayerBlockInput, BlockBreakEvent> predicate) {
        this.plugin = plugin;
        this.success = success;
        this.message = message;
        this.error = error;
        this.rainbowAPI = RainbowAPI.apis.get(plugin);
        this.predicate = predicate;
        if (inputMap.containsKey(player.getUniqueId())) {
            rainbowAPI.prefixUtil.send(player, "<red>すでにブロック入力中です");
        } else {
            rainbowAPI.prefixUtil.send(player, message);
            inputMap.put(player.getUniqueId(), this);
        }
    }

    public static boolean isInputted(Player player) {
        if (inputMap.containsKey(player.getUniqueId())) {
            return false;
        }
        RMHome.getRainbowAPI().prefixUtil.send(player, "<red>現在入力中です");
        return true;
    }

    public void build(BlockBreakEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        if (predicate.test(this, e)) {
            rainbowAPI.prefixUtil.send(p, success.apply(e));
            inputMap.remove(p);
        } else {
            rainbowAPI.prefixUtil.send(p, "<red>" + error);
            rainbowAPI.prefixUtil.send(p, message);
        }
    }
}
