package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.arguments.Argument;
import github.rainbowmori.rainbowapi.object.command.arguments.IGreedyArgument;
import github.rainbowmori.rainbowapi.object.command.arguments.MultiLiteralArgument;
import github.rainbowmori.rainbowapi.object.command.exceptions.GreedyArgumentException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandAPICommand extends ExecutableCommand<CommandAPICommand> {

	private List<Argument<?>> args = new ArrayList<>();
	private List<CommandAPICommand> subcommands = new ArrayList<>();
	private boolean isConverted;

	public CommandAPICommand(String commandName) {
		super(commandName);
		this.isConverted = false;
	}

	protected CommandAPICommand(CommandMetaData metaData) {
		super(metaData);
		this.isConverted = false;
	}

	public CommandAPICommand withArguments(List<Argument<?>> args) {
		this.args.addAll(args);
		return this;
	}

	public CommandAPICommand withArguments(Argument<?>... args) {
		this.args.addAll(Arrays.asList(args));
		return this;
	}

	public CommandAPICommand withSubcommand(CommandAPICommand subcommand) {
		this.subcommands.add(subcommand);
		return this;
	}

	public CommandAPICommand withSubcommands(CommandAPICommand... subcommands) {
		this.subcommands.addAll(Arrays.asList(subcommands));
		return this;
	}

	public List<Argument<?>> getArguments() {
		return args;
	}

	public void setArguments(List<Argument<?>> args) {
		this.args = args;
	}

	public List<CommandAPICommand> getSubcommands() {
		return subcommands;
	}

	public void setSubcommands(List<CommandAPICommand> subcommands) {
		this.subcommands = subcommands;
	}

	private static void flatten(Plugin plugin,CommandAPICommand rootCommand, List<Argument<?>> prevArguments, CommandAPICommand subcommand) {
		String[] literals = new String[subcommand.meta.aliases.length + 1];
		literals[0] = subcommand.meta.commandName;
		System.arraycopy(subcommand.meta.aliases, 0, literals, 1, subcommand.meta.aliases.length);

		MultiLiteralArgument literal = (MultiLiteralArgument) new MultiLiteralArgument(literals)
			.withPermission(subcommand.meta.permission)
			.withRequirement(subcommand.meta.requirements)
			.setListed(false);

		prevArguments.add(literal);

		if (subcommand.executor.hasAnyExecutors()) {
			rootCommand.args = prevArguments;
			rootCommand.withArguments(subcommand.args);
			rootCommand.executor = subcommand.executor;
			rootCommand.subcommands = new ArrayList<>();
			rootCommand.register(plugin);
		}

		for (CommandAPICommand subsubcommand : new ArrayList<>(subcommand.subcommands)) {
			flatten(plugin,rootCommand, new ArrayList<>(prevArguments), subsubcommand);
		}
	}

	@Override
	public void register(Plugin plugin) {
		try {
			Argument<?>[] argumentsArr = (args == null) ? new Argument<?>[0] : args.toArray(new Argument<?>[0]);

			for (int i = 0, numGreedyArgs = 0; i < argumentsArr.length; i++) {
				if (argumentsArr[i] instanceof IGreedyArgument) {
					if (++numGreedyArgs > 1 || i != argumentsArr.length - 1) {
						throw new GreedyArgumentException(argumentsArr);
					}
				}
			}

			for (Argument<?> argument : argumentsArr) {
				if (argument.getArgumentPermission() == null) {
					argument.withPermission(meta.permission);
				}
			}

			if (executor.hasAnyExecutors()) {
				CommandAPIHandler.getInstance().register(plugin,meta, argumentsArr, executor, isConverted);
			}

			// Convert subcommands into multiliteral arguments
			for (CommandAPICommand subcommand : new ArrayList<>(this.subcommands)) {
				flatten(plugin,this.copy(), new ArrayList<>(), subcommand);
			}
		} catch (CommandSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public CommandAPICommand copy() {
		CommandAPICommand command = new CommandAPICommand(new CommandMetaData(this.meta));
		command.args = new ArrayList<>(this.args);
		command.subcommands = new ArrayList<>(this.subcommands);
		command.isConverted = this.isConverted;
		return command;
	}
}
