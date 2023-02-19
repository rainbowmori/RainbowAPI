package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.command.CommandItemEdit;
import github.rainbowmori.rainbowapi.object.command.CommandAPIHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {


    private static RMHome plugin;

    private static RainbowAPI rainbowAPI;

    public static RMHome getPlugin() {
        return plugin;
    }

    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }

    @Override
    public void onEnable() {
        plugin = this;
        rainbowAPI = new RainbowAPI(this, "<gray>[<red>RM<gray>] ");
        CommandItemEdit.register();
    }

    @Override
    public void onDisable() {
        CommandAPIHandler.unregister("ie");
    }
}
