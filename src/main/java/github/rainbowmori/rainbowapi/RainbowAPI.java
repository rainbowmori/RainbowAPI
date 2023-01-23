package github.rainbowmori.rainbowapi;

import com.google.gson.Gson;
import github.rainbowmori.rainbowapi.listener.JoinQuitEvents;
import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.util.McUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class RainbowAPI {

    public static final Map<Plugin, RainbowAPI> apis = new HashMap<>();

    public static final PluginManager manager = Bukkit.getServer().getPluginManager();

    public static final Gson gson = new Gson();

    public final String prefix;

    public final JavaPlugin plugin;

    public final McUtil mcUtil;

    public RainbowAPI(final JavaPlugin plugin,final String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
        manager.registerEvents(GuiListener.getInstance(), plugin);
        manager.registerEvents(JoinQuitEvents.getInstance(), plugin);
        mcUtil = new McUtil(this);
        apis.put(plugin, this);
    }
}
