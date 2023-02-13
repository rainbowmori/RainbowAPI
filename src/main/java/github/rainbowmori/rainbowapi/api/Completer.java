package github.rainbowmori.rainbowapi.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public interface Completer extends TabCompleter {
    @Override
    @Nullable
    default List<String> onTabComplete(@NotNull CommandSender sender,
                                       @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = returned(sender, args);
        return args[args.length - 1].isEmpty() ? tab : tab.stream().filter(s -> s.toLowerCase().
                startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    List<String> returned(@NotNull CommandSender sender, @NotNull String[] args);
}
