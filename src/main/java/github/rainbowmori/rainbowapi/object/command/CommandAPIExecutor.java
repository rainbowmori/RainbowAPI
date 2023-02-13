package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.executor.ExecutorType;
import github.rainbowmori.rainbowapi.object.command.executor.IExecutorTyped;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandAPIExecutor {
    private List<IExecutorTyped> executors;

    public CommandAPIExecutor() {
        executors = new ArrayList<>();
    }

    public void addExecutor(IExecutorTyped executor) {
        this.executors.add(executor);
    }

    private boolean matches(List<? extends IExecutorTyped> executors, ExecutorType type) {
        for (IExecutorTyped executor : executors) {
            if (executor.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private void execute(List<? extends IExecutorTyped> executors, CommandSender sender, Command command, String label, String[] args,
                         ExecutorType type) {
        for (IExecutorTyped executor : executors) {
            if (executor.getType() == type) {
                executor.executeWith(sender, command, label, args);
                return;
            }
        }
    }

    public List<IExecutorTyped> getExecutors() {
        return executors;
    }

    public void setExecutors(List<IExecutorTyped> executors) {
        this.executors = executors;
    }

    public boolean hasAnyExecutors() {
        return !(executors.isEmpty());
    }
}
