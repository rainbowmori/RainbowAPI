package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.playerinput.PlayerBlockInput;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

  private static final BlockBreak INSTANCE = new BlockBreak();

  private BlockBreak() {
  }

  public static BlockBreak getInstance() {
    return INSTANCE;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void damage(BlockBreakEvent e) {
    UUID uuid = e.getPlayer().getUniqueId();
    PlayerBlockInput.build(uuid, e);
  }

}
