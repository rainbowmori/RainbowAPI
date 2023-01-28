package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class ConsumerAction implements MenuAction {
    private final BiConsumer<MenuHolder<?>, InventoryClickEvent> consumer;

    public ConsumerAction(BiConsumer<MenuHolder<?>, InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        consumer.accept(menu, event);
    }
}
