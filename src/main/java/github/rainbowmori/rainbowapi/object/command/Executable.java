package github.rainbowmori.rainbowapi.object.command;


import github.rainbowmori.rainbowapi.object.command.exceptions.WrapperCommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.executors.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

abstract class Executable<T extends Executable<T>> {

    protected CommandAPIExecutor<?> executor = new CommandAPIExecutor<>();

    public T executes(CommandExecutor executor, ExecutorType... types) {
        if (types == null || types.length == 0) {
            this.executor.addNormalExecutor(executor);
        } else {
            for (ExecutorType type : types) {
                this.executor.addNormalExecutor(new github.rainbowmori.rainbowapi.object.command.executors.CommandExecutor() {

                    @Override
                    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
                        executor.executeWith(sender, args);
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


    public T executes(ResultingCommandExecutor executor, ExecutorType... types) {
        if (types == null || types.length == 0) {
            this.executor.addResultingExecutor(executor);
        } else {
            for (ExecutorType type : types) {
                this.executor.addResultingExecutor(new ResultingCommandExecutor() {

                    @Override
                    public int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
                        return executor.executeWith(sender, args);
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

    public T executesPlayer(PlayerCommandExecutor executor) {
        this.executor.addNormalExecutor(executor);
        return (T) this;
    }

    public T executesPlayer(PlayerResultingCommandExecutor executor) {
        this.executor.addResultingExecutor(executor);
        return (T) this;
    }

    public T executesConsole(ConsoleCommandExecutor executor) {
        this.executor.addNormalExecutor(executor);
        return (T) this;
    }

    public T executesConsole(ConsoleResultingCommandExecutor executor) {
        this.executor.addResultingExecutor(executor);
        return (T) this;
    }

    public CommandAPIExecutor<? extends CommandSender> getExecutor() {
        return executor;
    }

    public void setExecutor(CommandAPIExecutor<? extends CommandSender> executor) {
        this.executor = executor;
    }

    public T clearExecutors() {
        this.executor.setNormalExecutors(new ArrayList<>());
        this.executor.setResultingExecutors(new ArrayList<>());
        return (T) this;
    }

}
