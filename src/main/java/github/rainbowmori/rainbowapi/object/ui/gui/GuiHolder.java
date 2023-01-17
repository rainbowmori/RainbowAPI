package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class GuiHolder<P extends JavaPlugin> implements InventoryHolder {

    protected final RMData RMData;

    private final Inventory inventory;

    private final P plugin;

    public GuiHolder(RMData RMData, P plugin, InventoryType type, String title) {
        this.RMData = RMData;
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, type, Util.mm(title));
    }

    public GuiHolder(RMData RMData, P plugin, int size, String title) {
        this.RMData = RMData;
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, size, Util.mm(title));
    }

    public GuiHolder(RMData RMData, P plugin, InventoryType type) {
        this.RMData = RMData;
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, type);
    }

    public GuiHolder(RMData RMData, P plugin, int size) {
        this.RMData = RMData;
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, size);
    }

    public GuiHolder(RMData RMData, P plugin, Inventory inventory) {
        this.RMData = RMData;
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.inventory = Objects.requireNonNull(inventory, "Inventory cannot be null");
    }

    public void open() {
        RMData.getPlayer().openInventory(getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public final P getPlugin() {
        return plugin;
    }

    public void onClose(InventoryCloseEvent event) {
    }

    public void onClick(InventoryClickEvent event) {
    }

    public void onOpen(InventoryOpenEvent event) {
    }

    public void onDrag(InventoryDragEvent event) {
    }

    protected static Inventory getClickedInventory(InventoryClickEvent event) {
        return getClickedInventory(event.getRawSlot(), event.getView());
    }

    protected static Inventory getClickedInventory(int rawSlot, InventoryView view) {
        if (rawSlot < 0) return null;
        Inventory topInventory = view.getTopInventory();
        if (rawSlot < topInventory.getSize()) return topInventory;
        return view.getBottomInventory();
    }
}
