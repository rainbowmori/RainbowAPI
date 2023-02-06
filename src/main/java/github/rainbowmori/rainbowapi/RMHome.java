package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.object.RMData;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    private static RMHome plugin;

    @Override
    public void onDisable() {
        plugin = this;
        plugin.getServer().getOnlinePlayers().forEach(RMData::new);
    }

    @Override
    public void onEnable() {

    }

    public static RMHome getPlugin() {
        return plugin;
    }
}
