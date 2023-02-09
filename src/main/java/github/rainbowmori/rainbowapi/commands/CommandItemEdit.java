package github.rainbowmori.rainbowapi.commands;

import github.rainbowmori.rainbowapi.RMHome;
import github.rainbowmori.rainbowapi.object.ChatEnum;
import github.rainbowmori.rainbowapi.util.IsObjectUtil;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.McUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandItemEdit implements CommandExecutor {
    private static final McUtil mcUtil = RMHome.getRainbowAPI().mcUtil;

    private static final Set<String> materials = Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toSet());

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatEnum.NOT_PLAYER.text);
            return true;
        }
        if (args.length == 0) {
            mcUtil.send(p,"<red>機能を入力してください");
            return true;
        }
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        ItemBuilder itemBuilder = new ItemBuilder(itemInMainHand);
        switch (args[0]) {
            case "rename" -> {
                if (checkArg(p, args, 1, "<red>変えたい名前を入力してください")) return true;
                itemBuilder.name(args[0]);
            }
            case "lore" -> {
                if(checkArg(p,args,1,"<red>add | set [line num] | remove これらの機能を使ってください")) return true;
                switch (args[0]) {
                    case "add" -> {
                        if (checkArg(p,args,2,"<red>loreに追加したい文字を入力してください")) return true;
                        itemBuilder.addLore(args[1]);
                    }
                    case "set" -> {
                        int i = IsObjectUtil.IsInt(args[1])-1;
                        if (itemBuilder.getLore().size()< i) {
                            mcUtil.send(p,"<red>loreの行はそんなにありません");
                            return true;
                        }
                        if(checkArg(p,args,3,"<red>セットしたい文字を入力してください")) return true;
                        itemBuilder.setLore(i - 1, args[2]);
                    }
                    case "remove" -> {
                        if (checkArg(p,args,2,"<red>削除したいloreのラインを入力してください(上が1で始まります)")) return true;
                        int i = IsObjectUtil.IsInt(args[1])-1;
                        if (itemBuilder.getLore().size()< i) {
                            mcUtil.send(p,"<red>loreの行はそんなにありません");
                            return true;
                        }
                        itemBuilder.removeLore(i - 1);
                    }
                    default -> {
                        mcUtil.send(p,"<red>入力ミスです");
                        return true;
                    }
                }

            }
            case "type" -> {
                if(checkArg(p,args,1,"<red>変更するタイプを入力してください")) return false;
                itemBuilder.type(Material.valueOf(args[0]));
            }
            case "amount" -> {
                if(checkArg(p,args,1,"<red>個数を入力してください")) return false;
                int i = IsObjectUtil.IsInt(args[0]);
                if (i == 0) {
                    mcUtil.send(p,"<red>数値を入力してください");
                    return true;
                }
                itemBuilder.amount(i);
            }
            case "durability" -> {
                if(checkArg(p,args,1,"<red>耐久値を入力してください")) return false;
                int i = IsObjectUtil.IsInt(args[0]);
                if (i == 0) {
                    mcUtil.send(p,"<red>数値を入力してください");
                    return true;
                }
                itemBuilder.changeMeta((Consumer< Damageable >) consumer ->
                        consumer.setDamage(itemInMainHand.getMaxItemUseDuration() - i));

            }
            default -> {
                mcUtil.send(p,"<red>入力ミスです");
                return true;
            }
        }
        p.getInventory().setItemInMainHand(itemBuilder.build());
        return false;
    }

    private boolean checkArg(Player player,String[] args, int number, String matchMessage) {
        if (args.length == number) {
            mcUtil.send(player,matchMessage);
            return true;
        }
        return false;
    }
}
