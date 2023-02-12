package github.rainbowmori.rainbowapi.object.command.executor;

import org.bukkit.entity.Player;

public interface PlayerExecutor extends Executor<Player> {
    @Override
    default ExecutorType getType() {
        return ExecutorType.PLAYER;
    }
}
