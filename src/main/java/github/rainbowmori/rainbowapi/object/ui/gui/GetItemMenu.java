package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.object.ui.button.ItemButton;
import github.rainbowmori.rainbowapi.object.ui.button.MenuButton;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class GetItemMenu<P extends JavaPlugin> extends MenuHolder<P> {
    protected static final ItemStack YES_STACK = new ItemBuilder(Material.LIME_CONCRETE).name("<green>アイテムを置きました").build();
    protected static final ItemStack TO_RIGHT_STACK = new ItemBuilder(Material.WHITE_BANNER).name("<blue>右にアイテムを置いてください").changeMeta((Consumer<BannerMeta>) itemMeta -> {
        itemMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        itemMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        itemMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        itemMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
    }).build();
    protected static final ItemStack TO_LEFT_STACK = new ItemBuilder(Material.WHITE_BANNER).name("<blue>左にアイテムを置いてください").changeMeta((Consumer<BannerMeta>) itemMeta -> {
        itemMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        itemMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        itemMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        itemMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
    }).build();
    protected static final ItemStack NO_STACK = new ItemBuilder(Material.RED_CONCRETE).name("<red>キャンセルします").build();
    protected Consumer<ItemStack> putItemAction;
    protected Consumer<Player> cancelAction;
    private boolean end = false;

    public GetItemMenu(P plugin, String question, Consumer<ItemStack> putItemAction, Consumer<Player> cancelAction) {
        super(plugin, Bukkit.createInventory(null, InventoryType.HOPPER, Util.mm(question)));
        this.putItemAction = putItemAction;
        this.cancelAction = cancelAction;
        setUpButton();
    }

    private void setUpButton() {
        setButton(0, makeYesButton());
        setButton(1, new ItemButton(TO_RIGHT_STACK));
        setButton(3, new ItemButton(TO_LEFT_STACK));
        setButton(4, makeNoButton());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!end) {
            getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                if (cancelAction != null) {
                    cancelAction.accept(((Player) event.getPlayer()));
                }
            });
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (!GuiListener.isGuiRegistered(getClickedInventory(event)) || event.getSlot() == 2) {
            return;
        }
        super.onClick(event);
    }

    protected MenuButton makeYesButton() {
        return new ItemButton(YES_STACK) {
            @Override
            public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
                end = true;
                getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                    event.getView().close();
                    if (putItemAction != null) {
                        putItemAction.accept(holder.getInventory().getItem(2));
                    }
                });
            }
        };
    }

    protected MenuButton makeNoButton() {
        return new ItemButton(NO_STACK) {
            @Override
            public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
                end = true;
                getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                    event.getView().close();
                    if (cancelAction != null) {
                        cancelAction.accept(((Player) event.getWhoClicked()));
                    }
                });
            }
        };
    }
}
