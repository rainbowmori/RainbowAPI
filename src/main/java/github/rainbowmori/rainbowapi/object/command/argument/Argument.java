package github.rainbowmori.rainbowapi.object.command.argument;

import github.rainbowmori.rainbowapi.object.command.executor.ConsoleExecutor;
import github.rainbowmori.rainbowapi.object.command.executor.ExecutorType;
import github.rainbowmori.rainbowapi.object.command.executor.IExecutorTyped;
import github.rainbowmori.rainbowapi.object.command.executor.PlayerExecutor;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Argument<T extends Argument<T>> implements TabCompleter, CommandExecutor {
    protected final List<Argument<?>> Args = new ArrayList<>();

    protected final List<IExecutorTyped> executors = new ArrayList<>();

    private static List<String> matchedList(String[] args, List<String> list) {
        return args[args.length - 1].isEmpty() ? list : list.stream().filter(s -> s.toLowerCase().
                startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    public T addArgument(Argument<?>... args) {
        Args.addAll(List.of(args));
        return (T) this;
    }

    public abstract List<String> getArg();

    public abstract boolean isArgMatch(String matched);

    public T addExecutors(IExecutorTyped... execute) {
        this.executors.addAll(Arrays.asList(execute));
        return (T) this;
    }

    public T addConsoleExecutors(ConsoleExecutor... execute) {
        this.executors.addAll(Arrays.asList(execute));
        return (T) this;
    }

    public T addPlayerExecutors(PlayerExecutor... execute) {
        this.executors.addAll(Arrays.asList(execute));
        return (T) this;
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
                executor.executeWith(sender, command,label,args);
                return;
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (executors.size() == 0) {
                sender.sendMessage(Util.mm("<red>入力ミスかまた入力してください"));
            } else {
                if (sender instanceof Player && matches(executors, ExecutorType.PLAYER)) {
                    execute(executors, sender, command, label, args, ExecutorType.PLAYER);
                } else if (sender instanceof ConsoleCommandSender && matches(executors, ExecutorType.CONSOLE)) {
                    execute(executors, sender, command, label, args, ExecutorType.CONSOLE);
                } else if (matches(executors, ExecutorType.ALL)) {
                    execute(executors, sender, command, label, args, ExecutorType.ALL);
                } else if (sender instanceof Player) {
                    sender.sendMessage(Util.mm("<red>このコマンドはコンソールのみ実行できます"));
                } else if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(Util.mm("<red>このコマンドはプレイヤーのみ実行できます"));
                }
            }
            return true;
        }
        for (Argument<?> argument : Args) {
            if (argument.isArgMatch(args[0])) {
                argument.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }
        sender.sendMessage(Util.mm("<red>引数を間違えています"));
        return true;
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (Args.size() == 0) {
                return Collections.singletonList("引数の入力をやめてください");
            }
            return matchedList(args, Args.stream().flatMap(argument -> argument.getArg().stream()).toList());
        }
        if (Args.size() == 0) {
            return Collections.singletonList("前の引数も入力をやめてください");
        }
        for (Argument<?> Arg : Args) {
            if (Arg.isArgMatch(args[0])) {
                return Arg.onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return Collections.singletonList("この前の引数を間違えています");
    }
}