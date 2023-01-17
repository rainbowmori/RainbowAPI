package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OPCommandAction extends MenuCommandAction {
    public OPCommandAction(String command) {
        super(command);
    }

    @Override
    public void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) Util.executeCommand(player, getCommand());
    }

    @Override
    public String toString() {
        return "op " + getCommand();
    }
}
