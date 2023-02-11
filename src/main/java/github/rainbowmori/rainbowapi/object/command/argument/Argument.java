package github.rainbowmori.rainbowapi.object.command.argument;

import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Argument<T extends Argument<T>> implements CommandExecutor {
    protected final List<Argument<?>> Args = new ArrayList<>();

    protected CommandExecutor execute;

    public T addArgument(Argument<?>... args) {
        Args.addAll(List.of(args));
        return (T) this;
    }

    public abstract List<String> getArg();

    public abstract boolean isArgMatch(String matched);

    public T setExecute(CommandExecutor execute) {
        this.execute = execute;
        return (T) this;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (execute == null) {
                sender.sendMessage(Util.mm("<red>引数を間違えているかこの後に引数を入力する必要があります"));
            } else {
                execute.onCommand(sender, command, label, args);
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
}