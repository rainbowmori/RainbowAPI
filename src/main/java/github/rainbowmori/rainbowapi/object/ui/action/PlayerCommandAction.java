package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerCommandAction extends MenuCommandAction {
    public PlayerCommandAction(String command) {
        super(command);
    }

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            player.performCommand(getCommand());
        }
    }

    @Override
    public String toString() {
        return "command " + getCommand();
    }
}
