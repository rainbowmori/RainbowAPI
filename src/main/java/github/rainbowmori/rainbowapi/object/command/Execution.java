package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.arguments.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

record Execution(List<Argument<?>> arguments, CommandAPIExecutor<? extends CommandSender> executor) {

	public void register(Plugin plugin, CommandMetaData meta) {
		CommandAPICommand command = new CommandAPICommand(meta).withArguments(arguments);
		command.setExecutor(executor);
		command.register(plugin);
	}

	public Execution prependedBy(Argument<?> argument) {
		List<Argument<?>> arguments = new ArrayList<>();
		arguments.add(argument);
		arguments.addAll(arguments());
		return new Execution(arguments, executor);
	}

}
