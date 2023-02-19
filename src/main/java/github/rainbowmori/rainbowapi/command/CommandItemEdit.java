package github.rainbowmori.rainbowapi.command;

import github.rainbowmori.rainbowapi.object.command.CommandTree;
import github.rainbowmori.rainbowapi.object.command.arguments.*;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandItemEdit {
    public static final String[] materials = Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new);

    public static void register() {
        new CommandTree("ie")
            .then(new LiteralArgument("rename")
                .then(new GreedyStringArgument("name").executesPlayer((sender, args) -> {
                    itemEdit(sender, builder -> builder.name(args[0]));
                })))
            .then(new LiteralArgument("type")
                .then(new MultiLiteralArgument(materials)
                    .executesPlayer((sender, args) -> {
                        itemEdit(sender, builder -> builder.type(Material.valueOf(args[0].toString())));
                    })))
            .then(new LiteralArgument("durability")
                .then(new IntegerArgument("durability")
                    .executesPlayer((sender, args) -> {
                        itemEdit(sender, builder -> {
                            int max = builder.build().getType().getMaxDurability();
                            int size = (int) args[0];
                            System.out.println(max +"*::" + size);
                            if (size > max) {
                                sender.sendMessage(Util.mm("<red>アイテムの最大耐久値より以下にしてください"));
                                return builder;
                            }
                            return builder.changeMeta((Consumer<Damageable>) consumer -> consumer.setDamage(max - size));
                        });
                    })))
            .register();
    }

    private static void itemEdit(Player player, Function<ItemBuilder, ItemBuilder> function) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(Util.mm("<red>アイテムを手に持ってください"));
            return;
        }
        player.getInventory().setItemInMainHand(function.apply(new ItemBuilder(item)).build());
    }
}
