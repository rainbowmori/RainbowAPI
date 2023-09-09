package github.rainbowmori.rainbowapi.object.customblock;

import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomModelBlock extends CustomBlock {

  private final ItemFrame frame;

  public CustomModelBlock(Location location) {
    super(location);
    location.getBlock().setType(Material.BARRIER);
    frame = location.getWorld().spawn(location, ItemFrame.class, frame -> {
      frame.setFacingDirection(BlockFace.UP);
      frame.setRotation(0, 0);
      frame.setItem(getItem(), false);
      frame.setFixed(true);
      frame.setGravity(false);
      frame.setVisible(false);
    });
  }

  public CustomModelBlock(Location location, JsonObject data) {
    this(location);
  }

  public CustomModelBlock(Location location, Player player) {
    this(location);
  }

  @Override
  public void clearData(Location location) {
    frame.remove();
    location.getBlock().setType(Material.AIR);
  }

  @Override
  public void leftClick(PlayerInteractEvent e) {
    Location loc = e.getClickedBlock().getLocation();
    dropItem(loc);
    remove();
  }

  public void dropItem(Location location) {
    location.getWorld().dropItemNaturally(location, getItem());
  }

  /**
   * ここでブロックを左クリックし壊した際に使用します
   * @return ドロップするアイテム
   */
  public abstract ItemStack getItem();

  public ItemFrame getFrame() {
    return frame;
  }
}
