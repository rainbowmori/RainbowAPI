package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PlayerBlockInput {
    public static final Map<Player, PlayerBlockInput> inputMap = new HashMap<>();

    private final Plugin plugin;

    private final RainbowAPI rainbowAPI;

    private final BiConsumer<RainbowAPI,BlockBreakEvent> consumer;

    public PlayerBlockInput(Player player, Plugin plugin,String message, BiConsumer<RainbowAPI,BlockBreakEvent> consumer) {
        this.plugin = plugin;
        this.rainbowAPI = RainbowAPI.apis.get(plugin);
        this.consumer = consumer;
        if (inputMap.containsKey(player)) rainbowAPI.mcUtil.send(player, "<red>すでにブロック入力中です");
        else {
            rainbowAPI.mcUtil.send(player,message);
            inputMap.put(player, this);
        }
    }
    public void build(BlockBreakEvent e) {
        consumer.accept(rainbowAPI,e);
    }

    public static boolean isInputted(Player player) {
        if (inputMap.containsKey(player)) return false;
        Util.sendRM(player,"<red>現在入力中です");
        return true;
    }
}
