package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public record ChatAction(String message) implements MenuAction {

    @Override
    public void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            player.chat(message());
        }
    }

    @Override
    public String toString() {
        return "chat " + message;
    }
}
