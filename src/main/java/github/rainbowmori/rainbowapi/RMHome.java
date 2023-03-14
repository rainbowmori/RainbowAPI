package github.rainbowmori.rainbowapi;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import github.rainbowmori.rainbowapi.listener.BlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    
    private static RainbowAPI rainbowAPI;
    
    
    /**
     * plugin instance
     * @return return RMHome Plugin instance
     */
    public static RMHome getPlugin() {
        return JavaPlugin.getPlugin(RMHome.class);
    }
    
    /**
     * RMHome's RainbowAPI
     * @return RMHome's RainbowAPI
     */
    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }
    
    /**
     * 初期化
     */
    @Override
    public void onEnable() {
        RainbowAPI.init(this);
        rainbowAPI = new RainbowAPI(this, "<gray>[<red>RM<gray>] ");
        RainbowAPI.manager.registerEvents(BlockBreak.getInstance(), this);
        
        CommandAPI.onEnable(this);
    }
    
    /**
     * disable
     */
    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        RainbowAPI.getGlowAPI().disable();
    }
    
    /**
     * onLoad
     */
    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig());
    }
}
