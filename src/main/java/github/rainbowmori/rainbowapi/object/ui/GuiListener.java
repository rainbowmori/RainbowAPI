package github.rainbowmori.rainbowapi.object.ui;

import github.rainbowmori.rainbowapi.object.ui.gui.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class GuiListener implements Listener {

    private static final GuiListener INSTANCE = new GuiListener();

    public static GuiListener getInstance() {
        return INSTANCE;
    }

    private GuiListener() {}

    private static final WeakHashMap<Inventory, WeakReference<GuiHolder<?>>> guiInventories = new WeakHashMap<>();

    public static boolean registerGui(GuiHolder<?> holder, Inventory inventory) {
        if (holder == inventory.getHolder()) return true;

        return guiInventories.putIfAbsent(inventory, new WeakReference<>(holder)) == null;
    }

    public static GuiHolder<?> getHolder(Inventory inventory) {
        if(inventory == null) return null;
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof GuiHolder<?>) return (GuiHolder<?>) holder;

        WeakReference<GuiHolder<?>> reference = guiInventories.get(inventory);
        if (reference == null) return null;
        return reference.get();
    }

    public static boolean isGuiRegistered(GuiHolder<?> holder, Inventory inventory) {
        return getHolder(inventory) == holder;
    }

    public static boolean isGuiRegistered(Inventory inventory) {
        return getHolder(inventory) != null;
    }

    private static void onGuiInventoryEvent(InventoryEvent event, Consumer<GuiHolder<?>> action) {
        GuiHolder<?> guiHolder = getHolder(event.getInventory());

        if (guiHolder != null && guiHolder.getPlugin().isEnabled()) {
            action.accept(guiHolder);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onOpen(event));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onClick(event));
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
