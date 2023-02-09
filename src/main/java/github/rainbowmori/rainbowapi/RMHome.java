package github.rainbowmori.rainbowapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    private static RMHome plugin;

    private static RainbowAPI rainbowAPI;

    @Override
    public void onEnable() {
        plugin = this;
        rainbowAPI = new RainbowAPI(this,"<gray>[<red>RM<gray>] ");
    }

    @Override
    public void onDisable() {
    }

    public static RMHome getPlugin() {
        return plugin;
    }

    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }
}
