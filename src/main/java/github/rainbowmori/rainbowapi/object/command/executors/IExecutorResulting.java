
package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;


public interface IExecutorResulting<T extends CommandSender> extends IExecutorTyped {
	

	@Override
	default int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		return this.run((T) sender, args);
	}

	int run(T sender, Object[] args) throws WrapperCommandSyntaxException;
	
}
