package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.menu.MenuHolder;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

/**
 * コンストラクタで指定されたサプライヤが提供するインベントリにリダイレクトするボタンです。
 * アイコンの素材は鉄の扉です。
 */
public class BackButton<P extends Plugin> extends RedirectItemButton<MenuHolder<P>> {

    private static final ItemStack BACK_BUTTON = new ItemBuilder(Material.IRON_DOOR).name("Back").build();

    /**
     * サプライヤーが提供するインベントリにリダイレクトするBackButtonを作成します。
     * @param to the inventory supplier
     */
    public BackButton(Supplier<? extends Inventory> to) {
        super(BACK_BUTTON, to);
    }

    /**
     * サプライヤーが提供するインベントリにリダイレクトするBackButtonを作成します。
     * @param name アイコンの名前
     * @param to the inventory supplier
     */
    public BackButton(String name, Supplier<? extends Inventory> to) {
        super(new ItemBuilder(Material.IRON_DOOR).name(name).build(), to);
    }

}
