package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.argument.Argument;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {

    private final List<Argument> arguments = new ArrayList<>();

    private final String commandName;

    public Command(String commandName) {
        this.commandName = commandName;
    }

    public Command then(Argument argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}
