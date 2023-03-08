package github.rainbowmori.rainbowapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.skytasul.glowingentities.GlowingEntities;
import github.rainbowmori.rainbowapi.api.serializer.ItemStackSerializer;
import github.rainbowmori.rainbowapi.api.serializer.LocationSerializer;
import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.util.PrefixUtil;
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
	
	private static GuiListener guiListener;
	
	private static GlowingEntities glowAPI;
	
	private static boolean isAllPluginLoaded;
	
	public final String prefix;
	public final JavaPlugin plugin;
	public final PrefixUtil prefixUtil;
	
	/**
	 * プラグインとメッセージにつくprefixです
	 *
	 * @param plugin 　あなたのプラグイン
	 * @param prefix 　message prefix
	 */
	
	public RainbowAPI(final JavaPlugin plugin, final String prefix) {
		this.plugin = plugin;
		this.prefix = prefix + "<reset>";
		prefixUtil = new PrefixUtil(prefix, plugin.getLogger());
		apis.put(plugin, this);
	}
	
	static void init(RMHome rmHome) {
		manager.registerEvents(guiListener = GuiListener.getInstance(), rmHome);
		glowAPI = new GlowingEntities(rmHome);
		Bukkit.getScheduler().runTask(rmHome, () -> isAllPluginLoaded = true);
	}
	
	public static boolean isIsAllPluginLoaded() {
		return isAllPluginLoaded;
	}
	
	public static GlowingEntities getGlowAPI() {
		return glowAPI;
	}
	
	public static GuiListener getGuiListener() {
		return guiListener;
	}
	
}
