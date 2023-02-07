package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OpenAction implements MenuAction {
    private final BiFunction<MenuHolder<?>, InventoryClickEvent, Inventory> redirect;

    protected OpenAction(Supplier<Inventory> redirect) {
        this(((menu, e) -> redirect.get()));
    }

    public OpenAction(BiFunction<MenuHolder<?>, InventoryClickEvent, Inventory> redirect) {
        this.redirect = Objects.requireNonNull(redirect, "Redirect cannot be null");
    }

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        menu.getPlugin().getServer().getScheduler().runTask(menu.getPlugin(), () -> {
            event.getView().close();
            HumanEntity player = event.getWhoClicked();
            Inventory to = to(menu, event);
            if (to != null) player.openInventory(to);
        });
    }

    public final Inventory to(MenuHolder<?> menu, InventoryClickEvent event) {
        return redirect.apply(menu, event);
    }
}
