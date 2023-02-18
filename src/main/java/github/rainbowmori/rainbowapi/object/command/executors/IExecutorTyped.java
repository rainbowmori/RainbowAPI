
package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;


public interface IExecutorTyped {
	

	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

	int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

}
