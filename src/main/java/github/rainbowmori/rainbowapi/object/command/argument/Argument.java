package github.rainbowmori.rainbowapi.object.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class Argument<T extends Argument<T>> implements CommandExecutor {
    protected final List<Argument<?>> Args = new ArrayList<>();

    protected BiConsumer<CommandSender,String[]> execute;

    public T addArgment(Argument<?>... args) {
        Args.addAll(List.of(args));
        return (T) this;
    }

    public abstract boolean isArgMatch(String matched);

    public T setExecute(BiConsumer<CommandSender, String[]> execute) {
        this.execute = execute;
        return (T) this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (Argument<?> argument : Args) {
            if (argument.isArgMatch(args[0])) {
                argument.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }
        execute.accept(sender,args);
        return true;
    }

    /*
    CommandAPIみたいなのをいつか作ろうこれはかなり面倒だ
    new CommandTree("betterbossbar")
                .withPermission(CommandPermission.OP)
                .then(new LiteralArgument("set")
                        .then(new NamespacedKeyArgument("id")
                                .then(new LiteralArgument("players")
                                        .then(new EntitySelectorArgument<Collection<Player>>("targets", EntitySelector.MANY_PLAYERS)
                                                .executes(this::setPlayers)
                                        )
                                )
                                .then(new LiteralArgument("style")
                                        .then(new MultiLiteralArgument("notched_6", "notched_10", "notched_12", "notched_20", "progress")
                                                .executes(this::setStyle)
                                        )
                                )
                                .then(new LiteralArgument("value")
                                        .then(new IntegerArgument("value")
                                                .executes(this::setValue)
                                        )
                                )
                                .then(new LiteralArgument("visible")
                                        .then(new BooleanArgument("visible")
                                                .executes(this::setVisible)
                                        )
                                )
                        )
                )
                .then(new LiteralArgument("remove")
                        .then(new NamespacedKeyArgument("id")
                                .executes(this::removeBossbar)
                        )
                )
                .then(new LiteralArgument("get")
                        .then(new NamespacedKeyArgument("id")
                                .then(new LiteralArgument("players")
                                        .executes(this::getPlayers)
                                )
                                .then(new LiteralArgument("visible")
                                        .executes(this::getVisible)
                                )
                                .then(new LiteralArgument("max")
                                        .executes(this::getMax)
                                )
                                .then(new LiteralArgument("value")
                                        .executes(this::getValue)
                                )
                        )
                )
                .then(new LiteralArgument("add")
                        .then(new NamespacedKeyArgument("id")
                                .then(new ChatComponentArgument("name")
                                        .executes(this::addBossbar)
                                )
                        )
                )
                .then(new LiteralArgument("list")
                        .executes(this::list)
                )
                .register();
     */
}
