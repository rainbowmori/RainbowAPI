package github.rainbowmori.rainbowapi.object.ui.gui;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.object.ui.button.MenuButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * MenuButtonがあるGUI
 * @param <P> あなたのプラグイン
 */

public class MenuHolder<P extends Plugin> extends GuiHolder<P> {

    private final MenuButton<?>[] buttons;
    protected boolean canceled = true;

    /**
     * InventoryTypeとtitleを設定して使用します
     * @param RMData 指定するプレイヤーデータ {@link RMData}
     * @param plugin あなたのプラグイン
     * @param type {@link InventoryType}
     * @param title inventory title
     */

    public MenuHolder(RMData RMData, P plugin, InventoryType type, String title) {
        super(RMData, plugin, type, title);
        this.buttons = new MenuButton[type.getDefaultSize()];
    }

    /**
     * チェストのようなインベントリーを作成しますスロットの数とタイトルです
     * @param RMData 指定するプレイヤーデータ {@link RMData}
     * @param plugin あなたのプラグイン
     * @param size inventory size
     * @param title inventory title
     */

    public MenuHolder(RMData RMData, P plugin, int size, String title) {
        super(RMData, plugin, size, title);
        this.buttons = new MenuButton[size];
    }

    /**
     * inventory typeだけを設定して使用します
     * @param RMData 指定するプレイヤーデータ {@link RMData}
     * @param plugin あなたのプラグイン
     * @param type {@link InventoryType}
     */

    public MenuHolder(RMData RMData, P plugin, InventoryType type) {
        super(RMData, plugin, type);
        this.buttons = new MenuButton[type.getDefaultSize()];
    }

    /**
     * チェストのスロットサイズだけを設定して使用します
     * @param RMData 指定するプレイヤーデータ {@link RMData}
     * @param plugin あなたのプラグイン
     * @param size inventory size
     */

    public MenuHolder(RMData RMData, P plugin, int size) {
        super(RMData, plugin, size);
        this.buttons = new MenuButton[size];
    }

    /**
     * すでに作成されているinventoryを設定して使用します
     * @param RMData 指定するプレイヤーデータ {@link RMData}
     * @param plugin あなたのプラグイン
     * @param inventory {@link org.bukkit.Bukkit#createInventory}
     */

    public MenuHolder(RMData RMData, P plugin, Inventory inventory) {
        super(RMData, plugin, inventory);
        this.buttons = new MenuButton[inventory.getSize()];
    }

    /**
     * this inventory click method
     * @param event このinventoryをクリックしたeventです
     */

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(canceled);
        Inventory clickedInventory = getClickedInventory(event);
        if (clickedInventory == null) return;
        getButtonOptionally(event.getSlot()).ifPresent((MenuButton button) -> button.onClick(this, event));
    }

    /**
     * get button of slot
     * @param slot 取得したいスロット
     * @return 取得したボタン
     */

    public MenuButton<?> getButton(int slot) {
        if (slot < 0 || slot >= getInventory().getSize()) return null;

        return this.buttons[slot];
    }

    /**
     * get button of optional
     * @param slot 取得したいスロット
     * @return 取得したボタン
     */

    public Optional<MenuButton<?>> getButtonOptionally(int slot) {
        return Optional.ofNullable(getButton(slot));
    }

    /**
     * put the button in the slot
     * @param slot slot
     * @param button button
     */

    public void setButton(int slot, MenuButton<?> button) {
        if (!unsetButton(slot) || button == null || !((MenuButton) button).onAdd(this, slot)) return;
        getInventory().setItem(slot, button.getIcon());
        this.buttons[slot] = button;
    }

    /**
     * remove the button in the slot
     * @param slot slot
     * @return 消えたかどうか
     */

    public boolean unsetButton(int slot) {
        MenuButton<?> menuButton = this.buttons[slot];
        if (menuButton == null) return true;
        if (!((MenuButton) menuButton).onRemove(this, slot)) return false;
        this.buttons[slot] = null;
        getInventory().setItem(slot, null);
        return true;
    }

    /**
     * すべてのボタンを消す
     */

    public void clearButtons() {
        IntStream.range(0, getInventory().getSize()).forEach(this::unsetButton);
    }
}
