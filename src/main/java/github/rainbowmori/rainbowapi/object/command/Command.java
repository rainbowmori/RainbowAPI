package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.argument.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Command extends Argument<Command> {
    private final String commandName;

    public Command(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public List<String> getArg() {
        return Collections.singletonList(commandName);
    }

    @Override
    public boolean isArgMatch(String matched) {
        return false;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        super.onCommand(sender, command, label, args);
        return true;
    }

    public void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(commandName);
        command.setExecutor(this);
    }
}
