
package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.ConsoleCommandSender;

@FunctionalInterface
public interface ConsoleCommandExecutor extends IExecutorNormal<ConsoleCommandSender> {

	void run(ConsoleCommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
