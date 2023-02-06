package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.object.RMData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    private static RMHome plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getOnlinePlayers().forEach(RMData::new);
    }

    @Override
    public void onDisable() {
    }

    public static RMHome getPlugin() {
        return plugin;
    }
}
