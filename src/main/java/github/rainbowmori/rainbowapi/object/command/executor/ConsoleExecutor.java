package github.rainbowmori.rainbowapi.object.command.executor;

import org.bukkit.command.ConsoleCommandSender;

public interface ConsoleExecutor extends Executor<ConsoleCommandSender>{
    @Override
    default ExecutorType getType() {
        return ExecutorType.CONSOLE;
    }
}
