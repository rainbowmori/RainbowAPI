package github.rainbowmori.rainbowapi.object.command.executors;

import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerCommandExecutor extends IExecutorNormal<Player> {


	void run(Player sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.PLAYER;
	}
}
