package github.rainbowmori.rainbowapi;

import github.rainbowmori.rainbowapi.object.ui.gui.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public class MenuListeners implements Listener {

    public static final MenuListeners INSTANCE = new MenuListeners();

    public static boolean clickCancelable(InventoryClickEvent event) {
        ClickType type = event.getClick();
        return !type.isLeftClick() && !type.isRightClick() || type.isCreativeAction();
    }

    public static GuiHolder<?> getHolder(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof GuiHolder) return (GuiHolder<?>) holder;
        return null;
    }

    public static boolean isGuiRegistered(GuiHolder<?> holder, Inventory inventory) {
        return getHolder(inventory) == holder;
    }

    public static boolean isGuiRegistered(Inventory inventory) {
        return getHolder(inventory) != null;
    }

    private void onGuiInventoryEvent(InventoryEvent event, Consumer<GuiHolder<?>> action) {
        GuiHolder<?> guiHolder = getHolder(event.getInventory());

        if (guiHolder != null && RainbowAPI.getPlugin().isEnabled()) {
            action.accept(guiHolder);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onOpen(event));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        onGuiInventoryEvent(event, gui -> {
            ClickType type = event.getClick();
            if(clickCancelable(event)) event.setCancelled(true);
            gui.onClick(event);
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        onGuiInventoryEvent(event, gui -> {
            event.setCancelled(true);
            gui.onDrag(event);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onClose(event));
    }

}
