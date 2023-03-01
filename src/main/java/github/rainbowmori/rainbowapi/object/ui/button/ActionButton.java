package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public class ActionButton implements MenuButton {
	
	private final BiConsumer<MenuHolder<?>, InventoryClickEvent> consumer;
	
	private final WeakHashMap<MenuHolder<?>, Set<Integer>> inventoriesContainingMe = new WeakHashMap<>();
	protected ItemStack stack;
	
	public ActionButton(BiConsumer<MenuHolder<?>, InventoryClickEvent> consumer) {
		this(null, consumer);
	}
	
	public ActionButton(ItemStack stack, BiConsumer<MenuHolder<?>, InventoryClickEvent> consumer) {
		this.stack = stack == null ? null : stack.clone();
		this.consumer = consumer;
	}
	
	public ActionButton(ItemStack stack) {
		this(stack, null);
	}
	
	public ActionButton clone(ItemStack item) {
		return new ActionButton(item, this.consumer);
	}
	
	@Override
	public final ItemStack getIcon() {
		return stack == null ? null : stack.clone();
	}
	
	public final void setIcon(ItemStack icon) {
		stack = icon == null ? null : icon.clone();
		inventoriesContainingMe.forEach((menuHolder, slots) -> slots.forEach(slot -> menuHolder.getInventory().setItem(slot, stack)));
	}
	
	@Override
	public final boolean onAdd(MenuHolder<?> menuHolder, int slot) {
		return inventoriesContainingMe.computeIfAbsent(menuHolder, mh -> new HashSet<>()).add(slot);
	}
	
	@Override
	public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
		if (consumer != null) {
			consumer.accept(holder,event);
		}
	}
	
	@Override
	public final boolean onRemove(MenuHolder<?> menuHolder, int slot) {
		Set<Integer> slots = inventoriesContainingMe.get(menuHolder);
		if (slots != null) {
			boolean result = slots.remove(slot);
			if (slots.isEmpty()) {
				inventoriesContainingMe.remove(menuHolder);
			}
			return result;
		}
		return true;
	}
}
