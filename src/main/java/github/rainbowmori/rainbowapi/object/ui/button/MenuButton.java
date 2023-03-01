package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuButton {

    default void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
    }

    default ItemStack getIcon() {
        return null;
    }

    default boolean onAdd(MenuHolder<?> menuHolder, int slot) {
        return true;
    }

    default boolean onRemove(MenuHolder<?>  menuHolder, int slot) {
        return true;
    }
}
