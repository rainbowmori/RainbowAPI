package github.rainbowmori.rainbowapi.object.command.executors;

import org.bukkit.command.ConsoleCommandSender;

@FunctionalInterface
public interface ConsoleResultingCommandExecutor extends IExecutorResulting<ConsoleCommandSender> {

	int run(ConsoleCommandSender sender, Object[] args);


	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
