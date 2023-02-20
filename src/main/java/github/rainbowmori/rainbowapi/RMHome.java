package github.rainbowmori.rainbowapi;

import com.google.common.base.Charsets;
import github.rainbowmori.rainbowapi.command.CommandItemEdit;
import github.rainbowmori.rainbowapi.object.commandapi.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public void onDisable() {
        CommandAPI.onDisable();
    }

    @Override
    public void onLoad() {
        CommandAPI.config = new InternalConfig(createFile(), new File(getDataFolder(), "command_registration.json"));

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

    private static final String CommandAPIConfig = "CommandAPI.yml";

    public FileConfiguration createFile() {
        saveResource(CommandAPIConfig,false);
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(
            new File(getDataFolder(),CommandAPIConfig));
        final InputStream defConfigStream = getResource(CommandAPIConfig);
        if (defConfigStream == null) {
            return null;
        }
        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        return newConfig;
    }
}
