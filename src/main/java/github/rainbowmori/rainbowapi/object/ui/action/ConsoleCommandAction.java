package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConsoleCommandAction extends MenuCommandAction {
    public ConsoleCommandAction(String command) {
        super(command);
    }

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            RainbowAPI.apis.get(menu.getPlugin()).mcUtil.consoleCommand(getCommand());
        }
    }

    @Override
    public String toString() {
        return "console " + getCommand();
    }
}
