package github.rainbowmori.rainbowapi.object.ui.button;

import github.rainbowmori.rainbowapi.object.ui.GUIClickType;
import github.rainbowmori.rainbowapi.object.ui.GuiListener;
import github.rainbowmori.rainbowapi.object.ui.action.MenuAction;
import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A button with an icon.
 *
 * @param <MH> the type of the menu
 */
public class ItemButton<MH extends MenuHolder<?>> implements MenuButton<MH> {

    protected final HashMap<GUIClickType, MenuAction> actions = new HashMap<>();
    private final WeakHashMap<MH, Set<Integer>> inventoriesContainingMe = new WeakHashMap<>();
    protected ItemStack stack;

    protected ItemButton() {
    }

    public ItemButton(ItemStack stack) {
        this.stack = stack == null ? null : stack.clone();
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
    public final boolean onAdd(MH menuHolder, int slot) {
        return inventoriesContainingMe.computeIfAbsent(menuHolder, mh -> new HashSet<>()).add(slot);
    }

    @Override
    public final boolean onRemove(MH menuHolder, int slot) {
        Set<Integer> slots = inventoriesContainingMe.get(menuHolder);
        if (slots != null) {
            boolean result = slots.remove(slot);
            if (slots.isEmpty()) inventoriesContainingMe.remove(menuHolder);
            return result;
        }
        return true;
    }

    public final MenuButton<?> setAction(GUIClickType clickType, MenuAction action) {
        actions.put(clickType, action);
        return this;
    }

    public final Optional<MenuAction> getAction(GUIClickType clickType) {
        return Optional.ofNullable(actions.get(clickType));
    }

    @Override
    public void onClick(MH holder, InventoryClickEvent event) {
        getAction(GUIClickType.ALL).ifPresentOrElse(action -> action.onClick(holder, event), () -> {
            if (!GuiListener.clickCancelable(event))
                getAction(GUIClickType.valueOf(event.getClick().name())).ifPresent(action -> action.onClick(holder, event));
        });
    }
}
