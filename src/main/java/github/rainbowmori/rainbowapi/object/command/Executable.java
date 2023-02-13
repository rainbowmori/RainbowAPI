package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.executor.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class Executable<T extends Executable<T>> {

    protected CommandAPIExecutor executor = new CommandAPIExecutor();

    public T executes(CommandExecutor executor, ExecutorType... types) {
        if(types == null || types.length == 0) {
            this.executor.addExecutor(executor);
        } else {
            for(ExecutorType type : types) {
                this.executor.addExecutor(new CommandExecutor() {
                    @Override
                    public void run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
                        executor.executeWith(sender, command, label, args);
                    }

                    @Override
                    public ExecutorType getType() {
                        return type;
                    }
                });
            }
        }
        return (T) this;
    }

    public T execute(IExecutorTyped execute) {
        this.executor.addExecutor(execute);
        return (T) this;
    }

    public T executeConsole(ConsoleExecutor execute) {
        this.executor.addExecutor(execute);
        return (T) this;
    }

    public T executePlayer(PlayerExecutor execute) {
        this.executor.addExecutor(execute);
        return (T) this;
    }

    public CommandAPIExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(CommandAPIExecutor executor) {
        this.executor = executor;
    }
    public T clearExecutor() {
        this.executor.setExecutors(new ArrayList<>());
        return (T) this;
    }

}
