package github.rainbowmori.rainbowapi;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.utils.MinecraftVersion;
import github.rainbowmori.rainbowapi.command.CommandItemEdit;
import github.rainbowmori.rainbowapi.object.commandapi.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

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
        CommandAPI.onEnable(this);
        CommandItemEdit.register();
    }

    @Override
    public void onLoad() {
        // Configure the NBT API - we're not allowing tracking at all, according
        // to the CommandAPI's design principles. The CommandAPI isn't used very
        // much, so this tiny proportion of servers makes very little impact to
        // the NBT API's stats.
        MinecraftVersion.disableBStats();
        MinecraftVersion.disableUpdateCheck();

        // Config loading
        CommandAPI.logger = getLogger();
        saveDefaultConfig();
        CommandAPI.config = new InternalConfig(getConfig(), NBTContainer.class, NBTContainer::new, new File(getDataFolder(), "command_registration.json"));

        // Check dependencies for CommandAPI
        CommandAPIHandler.getInstance().checkDependencies();

        // Convert all plugins to be converted
        for (Map.Entry<JavaPlugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
            if (pluginToConvert.getValue().length == 0) {
                Converter.convert(pluginToConvert.getKey());
            } else {
                for (String command : pluginToConvert.getValue()) {
                    new AdvancedConverter(pluginToConvert.getKey(), command).convert();
                }
            }
        }

        // Convert all arbitrary commands
        for (String commandName : CommandAPI.config.getCommandsToConvert()) {
            new AdvancedConverter(commandName).convertCommand();
        }
    }
}
