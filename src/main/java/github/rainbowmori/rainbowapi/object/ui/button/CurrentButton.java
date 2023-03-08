package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.menu.MenuHolder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class CurrentButton<MH extends MenuHolder<?>> extends ItemButton<MH>{
	
	private final Consumer<ItemStack> itemStackConsumer;
	
	public CurrentButton(ItemStack stack, Consumer<ItemStack> itemStackConsumer) {
		super(stack);
		this.itemStackConsumer = itemStackConsumer;
	}
	
	@Override
	public void onClick(MH holder, InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		if (cursor == null || cursor.getType() == Material.AIR) {
			event.getInventory().setItem(event.getSlot(), stack);
			return;
		}
		event.setCurrentItem(cursor);
	}
	
}
