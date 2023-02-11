package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.commands.CommandItemEdit;
import github.rainbowmori.rainbowapi.commands.CompleterItemEdit;
import github.rainbowmori.rainbowapi.object.command.Command;
import github.rainbowmori.rainbowapi.object.command.argument.ArgumentString;
import org.bukkit.command.PluginCommand;
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
        PluginCommand ie = getCommand("ie");
        ie.setExecutor(new CommandItemEdit());
        ie.setTabCompleter(new CompleterItemEdit());
        new Command("test").addArgument(new ArgumentString("aaa").setExecute((sender, command, label, args) -> {
            sender.sendMessage("a");
            return true;
        })).register(this);
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
