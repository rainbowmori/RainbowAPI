package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CloseAction implements MenuAction {

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        Bukkit.getScheduler().runTask(menu.getPlugin(),event.getView()::close);
    }

    @Override
    public String toString() {
        return "close";
    }
}
