package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimAction implements MenuAction {
    private final ItemStack item;
    private final boolean copy;

    public ClaimAction(ItemStack item) {
        this(item, false);
    }

    public ClaimAction(ItemStack item, boolean copy) {
        this.item = item;
        this.copy = copy;
    }


    @Override
    public final void onClick(MenuHolder<?> menuHolder, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        HumanEntity player = event.getWhoClicked();

        boolean success = clickedItem == null || player.getInventory().addItem(item).isEmpty();
        if (success && !copy) {
            event.setCurrentItem(null);
            menuHolder.unsetButton(event.getSlot());
        }
    }
}
