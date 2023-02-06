package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * GUIのInventoryHolder
 * <p>
 * このクラスは自由に拡張できます
 * {@link #onClick(InventoryClickEvent)},{@link #onDrag(InventoryDragEvent)},
 * {@link #onOpen(InventoryOpenEvent)},{@link #onClose(InventoryCloseEvent)} メゾットを
 * overrideするだけです
 * InventoryClickEvent と InventoryDragEvent はデフォルトでキャンセルされるように設定されていますが
 * {@code event.setCanceled(false)} を使用してサブクラスでキャンセルを取り消すことができます。
 * </p>
 * ボタン付きのメニューが必要な場合は、{@link MenuHolder} が適切な候補です。
 *
 * @param <P>あなたのプラグインです
 * @see MenuHolder
 */
public abstract class GuiHolder<P extends Plugin> implements InventoryHolder {
    private final Inventory inventory;

    private final P plugin;

    /**
     * InventoryTypeとtitleを設定して使用します
     * @param plugin あなたのプラグイン
     * @param type {@link InventoryType}
     * @param title inventory title
     */

    public GuiHolder(P plugin, InventoryType type, String title) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, type, Util.mm(title));
        GuiListener.registerGui(this, inventory);
    }

    /**
     * チェストのようなインベントリーを作成しますスロットの数とタイトルです
     * @param plugin あなたのプラグイン
     * @param size inventory size
     * @param title inventory title
     */

    public GuiHolder( P plugin, int size, String title) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, size, Util.mm(title));
        GuiListener.registerGui(this, inventory);
    }

    /**
     * inventory typeだけを設定して使用します
     * @param plugin あなたのプラグイン
     * @param type {@link InventoryType}
     */

    public GuiHolder( P plugin, InventoryType type) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, type);
        GuiListener.registerGui(this, inventory);
    }

    /**
     * チェストのスロットサイズだけを設定して使用します
     * @param plugin あなたのプラグイン
     * @param size inventory size
     */

    public GuiHolder( P plugin, int size) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, size);
        GuiListener.registerGui(this, inventory);
    }

    /**
     * すでに作成されているinventoryを設定して使用します
     * @param plugin あなたのプラグイン
     * @param inventory {@link org.bukkit.Bukkit#createInventory}
     */

    public GuiHolder( P plugin, Inventory inventory) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.inventory = Objects.requireNonNull(inventory, "Inventory cannot be null");
        GuiListener.registerGui(this, inventory);
    }

    /**
     * クリックしたinventoryを取得します
     * @param event 取得したいイベントです
     * @return clickedInventory クリックしたinventoryです
     */

    protected static Inventory getClickedInventory(InventoryClickEvent event) {
        return getClickedInventory(event.getRawSlot(), event.getView());
    }

    /**
     * クリックしたinventoryを取得します
     * @param rawSlot クリックしたraw slotです
     * @param view 取得したい{@link InventoryView}です
     * @return clickedInventory クリックしたinventoryです
     */

    protected static Inventory getClickedInventory(int rawSlot, InventoryView view) {
        if (rawSlot < 0) return null;
        Inventory topInventory = view.getTopInventory();
        if (rawSlot < topInventory.getSize()) return topInventory;
        return view.getBottomInventory();
    }

    /**
     * プレイヤーにinventoryを開きます
     * @param player open inventory to player
     */

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    /**
     * 使用しているinventoryを返します
     * @return inventory 使用しているinventory
     */

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * 使用しているPluginを返します
     * @return 使用しているPluginです
     */
    public final P getPlugin() {
        return plugin;
    }

    /**
     * inventoryを閉じたときにするメゾットです
     * @param event このinventoryを閉じたeventです
     */

    public void onClose(InventoryCloseEvent event) {
    }

    /**
     * inventoryをクリックしたときにするメゾットです
     * @param event このinventoryをクリックしたeventです
     */

    public void onClick(InventoryClickEvent event) {
    }

    /**
     * inventoryを開いたときにするメゾットです
     * @param event このinventoryを開いたeventです
     */

    public void onOpen(InventoryOpenEvent event) {
    }

    /**
     * アイテムをドラッグしたときにするメゾットです
     * @param event ドラッグしたイベントです
     */

    public void onDrag(InventoryDragEvent event) {
    }
}
