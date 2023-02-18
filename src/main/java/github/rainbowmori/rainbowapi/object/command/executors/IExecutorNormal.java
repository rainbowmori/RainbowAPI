
package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;


public interface IExecutorNormal<T extends CommandSender> extends IExecutorTyped {

	@Override
	default int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		this.run((T) sender, args);
		return 1;
	}

	void run(T sender, Object[] args) throws WrapperCommandSyntaxException;

}
