package github.rainbowmori.rainbowapi;

import com.google.gson.Gson;
import github.rainbowmori.rainbowapi.listener.JoinQuitEvents;
import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RainbowAPI extends JavaPlugin {
    public static final PluginManager manager = Bukkit.getServer().getPluginManager();

    public static final Gson gson = new Gson();

    private static String prefix;

    private static JavaPlugin plugin;

    public static String getPrefix() {
        return prefix;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(final JavaPlugin plugin,final String prefix) {
        RainbowAPI.plugin = plugin;
        RainbowAPI.prefix = prefix;
        manager.registerEvents(GuiListener.getInstance(), plugin);
        manager.registerEvents(new JoinQuitEvents(),plugin);
    }
}
