package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import github.rainbowmori.rainbowapi.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeleportAction implements MenuAction {

    private final Location location;

    public TeleportAction(Location location) {
        this.location = location;
    }

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        Bukkit.getScheduler().runTask(menu.getPlugin(), () -> player.teleport(getTo()));
    }

    public Location getTo() {
        return location.clone();
    }

    @Override
    public String toString() {
        return "teleport " + JsonUtil.encodeLocation(location);
    }

}
