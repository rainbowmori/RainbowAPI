package github.rainbowmori.rainbowapi.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Completer implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = returned(sender,args);
        return args[args.length - 1].isEmpty() ? tab : tab.stream().filter(s -> s.toLowerCase().
                startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    public abstract List<String> returned(@NotNull CommandSender sender, @NotNull String[] args);

    //predicateでエラーの場合にtabにエラーを表示する,かなり大規模になるから後で作る？多分内容忘れる
    /*
    * 
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    * */
}
