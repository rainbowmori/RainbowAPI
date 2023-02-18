package github.rainbowmori.rainbowapi.object.command;

import org.bukkit.command.CommandSender;

public record SuggestionInfo(

    CommandSender sender,

    Object[] previousArgs,

    String currentInput,

    String currentArg) {
}
