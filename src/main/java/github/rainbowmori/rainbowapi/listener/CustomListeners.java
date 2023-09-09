package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.customblock.CustomBlock;
import github.rainbowmori.rainbowapi.object.cutomitem.CustomItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CustomListeners implements Listener {

  private static final CustomListeners INSTANCE = new CustomListeners();

  private CustomListeners() {
  }

  public static CustomListeners getInstance() {
    return INSTANCE;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void interact(PlayerInteractEvent e) {
    EquipmentSlot hand = e.getHand();
    if (hand != null && hand.equals(EquipmentSlot.HAND) && !customItemInteract(e)) {
      customBlockInteract(e);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void swapItem(PlayerItemHeldEvent e) {
    Player player = e.getPlayer();
    PlayerInventory inventory = player.getInventory();
    ItemStack previousItem = inventory.getItem(e.getPreviousSlot());
    ItemStack newItem = inventory.getItem(e.getNewSlot());
    if (CustomItem.isCustomItem(previousItem)) {
      CustomItem.getCustomItem(previousItem).heldOfOther(e);
    }
    if (CustomItem.isCustomItem(newItem)) {
      CustomItem.getCustomItem(newItem).heldOfThis(e);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void shiftItem(PlayerToggleSneakEvent e) {
    Player player = e.getPlayer();
    ItemStack item = player.getInventory().getItemInMainHand();
    if (!CustomItem.isCustomItem(item)) {
      return;
    }
    CustomItem customItem = CustomItem.getCustomItem(item);
    customItem.shiftItem(e);
  }

  public boolean customItemInteract(PlayerInteractEvent e) {
    ItemStack item = e.getItem();
    if (!CustomItem.isCustomItem(item)) {
      return false;
    }
    e.setCancelled(true);
    CustomItem customItem = CustomItem.getCustomItem(item);
    if (e.getAction().isLeftClick()) {
      customItem.leftClick(e);
    } else {
      customItem.rightClick(e);
    }
    return true;
  }

  public boolean customBlockInteract(PlayerInteractEvent e) {
    Block block = e.getClickedBlock();
    if (block == null) {
      return false;
    }
    if (!CustomBlock.isCustomBlock(block)) {
      return false;
    }
    e.setCancelled(true);
    CustomBlock customBlock = CustomBlock.getCustomBlock(block);
    if (e.getAction().isLeftClick()) {
      customBlock.leftClick(e);
    } else {
      customBlock.rightClick(e);
    }
    return true;
  }
}
