package github.rainbowmori.rainbowapi.dependencies.ui.button;

import github.rainbowmori.rainbowapi.dependencies.ui.menu.MenuHolder;
import java.util.function.BiConsumer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * アイテムを持ってクリックするとそのアイテムで {@link #itemStackConsumer} が実行され 何も持たずにすると {@link #nullConsumer} が実行されます
 *
 * @param <MH> the MenuHolder type
 */
public class CurrentButton<MH extends MenuHolder<?>> extends ItemButton<MH> {

  private final BiConsumer<ItemStack, InventoryClickEvent> itemStackConsumer;
  private final BiConsumer<MH, InventoryClickEvent> nullConsumer;

  public CurrentButton(ItemStack stack,
      BiConsumer<ItemStack, InventoryClickEvent> itemStackConsumer,
      BiConsumer<MH, InventoryClickEvent> nullConsumer) {
    super(stack);
    this.itemStackConsumer = itemStackConsumer;
    this.nullConsumer = nullConsumer;
  }

  @Override
  public void onClick(MH holder, InventoryClickEvent event) {
    ItemStack cursor = event.getCursor();
    if (cursor == null || cursor.getType() == Material.AIR) {
      event.getInventory().setItem(event.getSlot(), stack);
      nullConsumer.accept(holder, event);
      return;
    }
    itemStackConsumer.accept(cursor, event);
    event.setCurrentItem(cursor);
  }

}
