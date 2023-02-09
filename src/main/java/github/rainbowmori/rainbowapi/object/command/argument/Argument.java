package github.rainbowmori.rainbowapi.object.command.argument;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Argument {
    private final List<Argument> arguments = new ArrayList<>();

    public Argument then(Argument argument) {
        arguments.add(argument);
        return this;
    }

    public abstract Argument execute(CommandSender sender, String[] args);
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
