package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.object.command.CommandTree;
import github.rainbowmori.rainbowapi.object.command.arguments.LiteralArgument;
import github.rainbowmori.rainbowapi.object.command.arguments.MultiLiteralArgument;
import github.rainbowmori.rainbowapi.object.command.arguments.TextArgument;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    private static Logger log;

    private static RMHome plugin;

    private static RainbowAPI rainbowAPI;

    public static RMHome getPlugin() {
        return plugin;
    }

    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }

    public static void logInfo(String message) {
        getLog().info(message);
    }

    public static void logNormal(String message) {
        getLog().info(message);
    }

    public static void logWarning(String message) {
        getLog().warning(message);
    }

    public static void logError(String message) {
        getLog().severe(message);
    }

    public static Logger getLog() {
        return log;
    }

    @Override
    public void onEnable() {
        plugin = this;
        log = getLogger();
        rainbowAPI = new RainbowAPI(this, "<gray>[<red>RM<gray>] ");
        commandRegister();
    }

    @Override
    public void onDisable() {
    }

    private void commandRegister() {
        new CommandTree("ie")
            .then(new LiteralArgument("rename")
                .then(new TextArgument("name").executesPlayer((sender, args) -> {
                    itemEdit(sender, builder -> builder.name(args[0]));
                })))
            .then(new LiteralArgument("type")
                .then(new MultiLiteralArgument(Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new))
                    .executesPlayer((sender, args) -> {
                        itemEdit(sender, builder -> builder.type(Material.valueOf(args[0].toString())));
                    })))
//            .then(new LiteralArgument("durability")
//                .then(new IntegerArgument("durability")
//                    .executesPlayer((sender, args) -> {
//                        itemEdit(sender, builder -> {
//                            int max = builder.build().getMaxItemUseDuration();
//                            int size = (int) args[0];
//                            if (size > max) {
//                                sender.sendMessage(Util.mm("<red>アイテムの最大耐久値より以下にしてください"));
//                                return builder;
//                            }
//                            return builder.changeMeta((Consumer<org.bukkit.inventory.meta.Damageable>) consumer -> consumer.setDamage(max - size));
//                        });
//                    })))
            .register(plugin);
    }

    private void itemEdit(Player player, Function<ItemBuilder, ItemBuilder> function) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(Util.mm("<red>アイテムを手に持ってください"));
            return;
        }
        player.getInventory().setItemInMainHand(function.apply(new ItemBuilder(item)).build());
    }
}
