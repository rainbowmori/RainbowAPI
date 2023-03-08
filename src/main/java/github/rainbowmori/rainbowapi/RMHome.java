package github.rainbowmori.rainbowapi;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import github.rainbowmori.rainbowapi.listener.BlockBreak;
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
        RainbowAPI.init(this);
        plugin = this;
        rainbowAPI = new RainbowAPI(this, "<gray>[<red>RM<gray>] ");
        RainbowAPI.manager.registerEvents(BlockBreak.getInstance(), plugin);
        
        CommandAPI.onEnable(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        RainbowAPI.getGlowAPI().disable();
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig());
    }
}
