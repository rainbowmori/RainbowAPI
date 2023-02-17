package github.rainbowmori.rainbowapi;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.PluginBrigadierCommand;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.data.registries.VanillaRegistries;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * RainbowAPI Plugin の main class
 */
public class RMHome extends JavaPlugin {

    private static RMHome plugin;

    private static RainbowAPI rainbowAPI;

    public static RMHome getPlugin() {
        return plugin;
    }

    public static RainbowAPI getRainbowAPI() {
        return rainbowAPI;
    }

    @Override
    public void onEnable() {
        plugin = this;
        rainbowAPI = new RainbowAPI(this, "<gray>[<red>RM<gray>] ");
        commandRegister();
    }

    @Override
    public void onDisable() {
    }

    private void commandRegister() {
        PluginBrigadierCommand.registerPluginBrigadierCommand(this, "ie",
            literal -> literal
                .then(literal("rename")
                    .then(argument("arg", StringArgumentType.string())
                        .executes(ctx -> itemEdit(ctx, builder -> builder.name(StringArgumentType.getString(ctx, "arg"))))))
                .then(literal("type")
                    .then(argument("material", BlockStateArgument.block(Commands.createValidationContext(VanillaRegistries.createLookup())))
                        .executes(ctx -> itemEdit(ctx, builder -> builder.type(BlockStateArgument.getBlock(ctx, "material").getState().getBukkitMaterial())))))
                .then(literal("amount")
                    .then(argument("size", IntegerArgumentType.integer(1, 127))
                        .executes(ctx -> itemEdit(ctx, builder -> builder.amount(IntegerArgumentType.getInteger(ctx, "size"))))))
                .then(literal("durability")
                    .then(argument("size", IntegerArgumentType.integer(1))
                        .executes(ctx -> itemEdit(ctx, builder -> {
                            int max = builder.build().getMaxItemUseDuration();
                            int size = IntegerArgumentType.getInteger(ctx, "size");
                            if (size > max) {
                                ctx.getSource().getBukkitSender().sendMessage(Util.mm("<red>アイテムの最大耐久値より以下にしてください"));
                                return builder;
                            }
                            return builder.changeMeta((Consumer<Damageable>) consumer ->
                                consumer.setDamage(max - size));
                        }))))
                .then(literal("lore")
                    .then(literal("add")
                        .then(argument("arg", StringArgumentType.string())
                            .executes(ctx -> itemEdit(ctx, builder -> builder.addLore(StringArgumentType.getString(ctx, "arg")))))))
                .then(literal("set")
                    .then(argument("size", IntegerArgumentType.integer(1))
                        .then(argument("arg", StringArgumentType.string())
                            .executes(ctx -> itemEdit(ctx, builder -> builder.setLore(
                                IntegerArgumentType.getInteger(ctx, "size") - 1, StringArgumentType.getString(ctx, "arg")))))))
                .then(literal("remove")
                    .then(argument("size", IntegerArgumentType.integer(1))
                        .executes(ctx -> itemEdit(ctx, builder -> {
                            int size = IntegerArgumentType.getInteger(ctx, "size") - 1;
                            if (size > builder.getLore().size()) {
                                ctx.getSource().getBukkitSender().sendMessage(Util.mm("<red>アイテムのloreの行以下にしてください"));
                                return builder;
                            }
                            return builder.removeLore(size);
                        })))));
    }

    private Player onlyPlayer(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getBukkitSender() instanceof Player player)) {
            context.getSource().getBukkitSender().sendMessage(Util.mm("<red>このコマンドはプレイヤーしか実行できません"));
            return null;
        }
        return player;
    }

    private int itemEdit(CommandContext<CommandSourceStack> context, Function<ItemBuilder, ItemBuilder> function) {
        Player player = onlyPlayer(context);
        if (player == null) {
            return 0;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(Util.mm("<red>アイテムを手に持ってください"));
            return 0;
        }
        player.getInventory().setItemInMainHand(function.apply(new ItemBuilder(item)).build());
        return Command.SINGLE_SUCCESS;
    }
}
