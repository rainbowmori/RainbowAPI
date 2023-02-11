package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuButton<MH extends MenuHolder<?>> {

    default void onClick(MH holder, InventoryClickEvent event) {
    }

    default ItemStack getIcon() {
        return null;
    }

    default boolean onAdd(MH menuHolder, int slot) {
        return true;
    }

    default boolean onRemove(MH menuHolder, int slot) {
        return true;
    }
}
