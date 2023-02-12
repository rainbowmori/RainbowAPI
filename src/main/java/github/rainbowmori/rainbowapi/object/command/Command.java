package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.argument.Argument;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class Command extends Argument<Command> {
    public final String commandName;

    public Command(String commandName) {
        this.commandName = commandName;
    }

    @Deprecated
    @Override
    public List<String> getArg() {
        return Collections.singletonList(commandName);
    }

    @Override
    public boolean isArgMatch(String matched) {
        return false;
    }

    public void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(commandName);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
