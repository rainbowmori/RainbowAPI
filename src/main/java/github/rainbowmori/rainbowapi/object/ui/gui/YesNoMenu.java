package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.ui.button.ItemButton;
import github.rainbowmori.rainbowapi.object.ui.button.MenuButton;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class YesNoMenu<P extends JavaPlugin> extends MenuHolder<P> {
    private static final ItemStack YES_STACK = new ItemBuilder(Material.LIME_CONCRETE).name("Yes - continue").build();
    private static final ItemStack NO_STACK = new ItemBuilder(Material.RED_CONCRETE).name("No - cancel").build();

    protected Consumer<Player> yesAction, noAction;

    private boolean end = false;

    /**
     * YesNoMenuを作成
     *
     * @param plugin    your plugin
     * @param question  inventory title
     * @param yesAction action clicked yes
     * @param noAction  action clicked no
     */

    public YesNoMenu(P plugin, String question, Consumer<Player> yesAction, Consumer<Player> noAction) {
        super(plugin, Bukkit.createInventory(null, InventoryType.HOPPER, Util.mm(question)));
        this.yesAction = yesAction;
        this.noAction = noAction;
        setupButtons();
    }

    /**
     * 閉じたときにnoActionを実行
     *
     * @param event このinventoryを閉じたeventです
     */

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!end) {
            getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                if (noAction != null) {
                    noAction.accept(((Player) event.getPlayer()));
                }
            });
        }
    }

    /**
     * yes and no button setup
     */

    protected void setupButtons() {
        setButton(0, makeButton(true));
        setButton(getInventory().getSize() - 1, makeButton(false));
    }

    /**
     * yes or no button make
     *
     * @param yesOrNo yes or no boolean
     * @return yes or no button
     */

    protected MenuButton makeButton(boolean yesOrNo) {
        ItemStack stack = yesOrNo ? YES_STACK : NO_STACK;
        Consumer<Player> action = yesOrNo ? yesAction : noAction;

        return new ItemButton(stack) {
            @Override
            public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
                end = true;
                getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                    event.getView().close();
                    if (action != null) {
                        action.accept(((Player) event.getWhoClicked()));
                    }
                });
            }
        };
    }
}