package github.rainbowmori.rainbowapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import github.rainbowmori.rainbowapi.api.serializer.ItemStackSerializer;
import github.rainbowmori.rainbowapi.api.serializer.LocationSerializer;
import github.rainbowmori.rainbowapi.listener.BlockDamage;
import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.util.McUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * APIのmain class
 * 使い方は{@link #RainbowAPI(JavaPlugin, String)}
 */

public final class RainbowAPI {
    public static final Map<Plugin, RainbowAPI> apis = new HashMap<>();

    public static final PluginManager manager = Bukkit.getServer().getPluginManager();

    public static final Gson gson = new GsonBuilder().
            registerTypeAdapter(ItemStack.class, new ItemStackSerializer()).
            registerTypeAdapter(Location.class, new LocationSerializer()).create();

    public final String prefix;

    public final JavaPlugin plugin;

    public final McUtil mcUtil;

    /**
     * プラグインとメッセージにつくprefixです
     *
     * @param plugin 　あなたのプラグイン
     * @param prefix 　message prefix
     */

    public RainbowAPI(final JavaPlugin plugin, final String prefix) {
        this.plugin = plugin;
        this.prefix = prefix + " <reset>";
        manager.registerEvents(GuiListener.getInstance(), plugin);
        manager.registerEvents(BlockDamage.getInstance(), plugin);
        mcUtil = new McUtil(prefix,plugin.getLogger());
        apis.put(plugin, this);
    }
}
