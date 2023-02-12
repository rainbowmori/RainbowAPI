
package github.rainbowmori.rainbowapi.object.command.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface IExecutorTyped {

	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

	void executeWith(CommandSender sender, Command command, String label, String[] args);

}
