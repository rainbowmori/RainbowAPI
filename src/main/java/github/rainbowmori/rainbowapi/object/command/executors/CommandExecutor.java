package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;


@FunctionalInterface
public interface CommandExecutor extends IExecutorNormal<CommandSender> {



	void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException;


	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
}
