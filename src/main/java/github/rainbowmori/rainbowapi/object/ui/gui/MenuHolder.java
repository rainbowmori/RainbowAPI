package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.object.ui.button.MenuButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Optional;

public class MenuHolder<P extends Plugin> extends GuiHolder<P> {

    private final MenuButton<?>[] buttons;
    protected boolean canceled = true;

    public MenuHolder(RMData RMData, P plugin, InventoryType type, String title, MenuButton<?>[] buttons) {
        super(RMData, plugin, type, title);
        this.buttons = Objects.requireNonNullElse(buttons, new MenuButton[type.getDefaultSize()]);
    }

    public MenuHolder(RMData RMData, P plugin, int size, String title, MenuButton<?>[] buttons) {
        super(RMData, plugin, size, title);
        this.buttons = Objects.requireNonNullElse(buttons, new MenuButton[size]);
    }

    public MenuHolder(RMData RMData, P plugin, InventoryType type, MenuButton<?>[] buttons) {
        super(RMData, plugin, type);
        this.buttons = Objects.requireNonNullElse(buttons, new MenuButton[type.getDefaultSize()]);
    }

    public MenuHolder(RMData RMData, P plugin, int size, MenuButton<?>[] buttons) {
        super(RMData, plugin, size);
        this.buttons = Objects.requireNonNullElse(buttons, new MenuButton[size]);
    }

    public MenuHolder(RMData RMData, P plugin, Inventory inventory, MenuButton<?>[] buttons) {
        super(RMData, plugin, inventory);
        this.buttons = Objects.requireNonNullElse(buttons, new MenuButton[inventory.getSize()]);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(canceled);
        Inventory clickedInventory = getClickedInventory(event);
        if (clickedInventory == null) return;
        getButtonOptionally(event.getSlot()).ifPresent((MenuButton button) -> button.onClick(this, event));
    }

    public MenuButton<?> getButton(int slot) {
        if (slot < 0 || slot >= getInventory().getSize()) return null;

        return this.buttons[slot];
    }

    public Optional<MenuButton<?>> getButtonOptionally(int slot) {
        return Optional.ofNullable(getButton(slot));
    }

    public void setButton(int slot, MenuButton<?> button) {
        if (!unsetButton(slot) || button == null || !((MenuButton) button).onAdd(this, slot)) return;
        getInventory().setItem(slot, button.getIcon());
        this.buttons[slot] = button;
    }

    public boolean unsetButton(int slot) {
        MenuButton<?> menuButton = this.buttons[slot];
        if (menuButton == null) return true;
        if (!((MenuButton) menuButton).onRemove(this, slot)) return false;
        this.buttons[slot] = null;
        getInventory().setItem(slot, null);
        return true;
    }

    public void clearButtons() {
        for (int i = 0; i < getInventory().getSize(); i++) {
            unsetButton(i);
        }
    }
}
