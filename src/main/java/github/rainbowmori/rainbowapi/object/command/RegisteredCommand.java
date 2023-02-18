package github.rainbowmori.rainbowapi.object.command;

import java.util.List;
import java.util.Optional;

public record RegisteredCommand(
		String commandName,

		List<String> argsAsStr,

		Optional<String> shortDescription,

		Optional<String> fullDescription,

		String[] aliases,

		CommandPermission permission) {
}