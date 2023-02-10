package github.rainbowmori.rainbowapi.commands;

import github.rainbowmori.rainbowapi.api.Completer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompleterItemEdit extends Completer {
    @Override
    public List<String> returned(@NotNull CommandSender sender, @NotNull String[] args) {
        switch (args.length) {
            case 0 -> {
                return List.of("rename", "lore", "type", "amount", "durability");
            }
            case 1 -> {
                switch (args[0]) {
                    case "lore" -> {
                        return List.of("add", "set", "remove");
                    }
                    case "type" -> {
                        return CommandItemEdit.materials.stream().toList();
                    }
                }
            }
        }
        return null;
    }
}
