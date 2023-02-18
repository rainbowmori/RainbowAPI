package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.exceptions.InvalidCommandNameException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

final class CommandMetaData {

	final String commandName;

	CommandPermission permission = CommandPermission.NONE;

	String[] aliases = new String[0];

	Predicate<CommandSender> requirements = s -> true;

	Optional<String> shortDescription = Optional.empty();

	Optional<String> fullDescription = Optional.empty();


	CommandMetaData(final String commandName) {
		if(commandName == null || commandName.isEmpty() || commandName.contains(" ")) {
			throw new InvalidCommandNameException(commandName);
		}

		this.commandName = commandName;
	}
	
	public CommandMetaData(CommandMetaData original) {
		this(original.commandName);
		this.permission = original.permission;
		this.aliases = Arrays.copyOf(original.aliases, original.aliases.length);
		this.requirements = original.requirements;
		this.shortDescription = original.shortDescription;
		this.fullDescription = original.fullDescription;
	}

}