package github.rainbowmori.rainbowapi.object.command.executor;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Executor<T extends CommandSender> extends IExecutorTyped {

    @Override
    default void executeWith(CommandSender sender, Command command, String label, String[] args) {
        this.run((T) sender, command,label,args);
    }
    void run(T sender, Command command, String label, String[] args);
}
