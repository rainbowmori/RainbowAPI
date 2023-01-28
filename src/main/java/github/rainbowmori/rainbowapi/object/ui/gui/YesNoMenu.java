package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.ui.button.ItemButton;
import github.rainbowmori.rainbowapi.object.ui.button.MenuButton;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class YesNoMenu<P extends Plugin> extends MenuHolder<P> {
    private static final ItemStack YES_STACK = new ItemBuilder(Material.LIME_CONCRETE).name("はい - 続ける").build();
    private static final ItemStack NO_STACK = new ItemBuilder(Material.RED_CONCRETE).name("いいえ - キャンセル").build();

    protected Consumer<InventoryClickEvent> yesAction, noAction;

    public YesNoMenu(github.rainbowmori.rainbowapi.object.RMData data, P plugin, String question, Consumer<InventoryClickEvent> yesAction, Consumer<InventoryClickEvent> noAction) {
        super(data, plugin, Bukkit.createInventory(null, InventoryType.HOPPER, Util.mm(question)), null);
        this.yesAction = yesAction;
        this.noAction = noAction;
        setupButtons();
    }

    protected void setupButtons() {
        setButton(0, makeButton(true));
        setButton(getInventory().getSize() - 1, makeButton(false));
    }

    protected MenuButton<YesNoMenu<P>> makeButton(boolean yesOrNo) {
        ItemStack stack = yesOrNo ? YES_STACK : NO_STACK;
        Consumer<InventoryClickEvent> action = yesOrNo ? yesAction : noAction;

        return new ItemButton<>(stack) {
            @Override
            public void onClick(YesNoMenu<P> holder, InventoryClickEvent event) {
                getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {
                    event.getView().close();
                    if (action != null) {
                        action.accept(event);
                    }
                });
            }
        };
    }

}