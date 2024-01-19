package github.rainbowmori.rainbowapi.object.playerinput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public abstract class PlayerBlockInput {

  private static final Map<UUID, PlayerBlockInput> inputMap = new HashMap<>();
  protected final Player player;

  public PlayerBlockInput(Player player) {
    if (inputMap.containsKey(player.getUniqueId())) {
      throw new RuntimeException("ブロック入力中なのにまた入力のメゾットを使用しています player = " + player.getName());
    } else {
      inputMap.put(player.getUniqueId(), this);
    }
    this.player = player;
  }

  public static boolean isInputted(Player player) {
    return isInputted(player.getUniqueId());
  }

  public static boolean isInputted(UUID uuid) {
    return inputMap.containsKey(uuid);
  }

  public static void build(UUID uuid, BlockBreakEvent e) {
    if (isInputted(uuid)) {
      inputMap.get(uuid).build(e);
    }
  }

  public void build(BlockBreakEvent e) {
    Player p = e.getPlayer();
    e.setCancelled(true);
    if (predicate(e)) {
      success(e);
      inputMap.remove(p.getUniqueId());
    } else {
      error(e);
    }
  }

  public abstract void error(BlockBreakEvent e);

  public abstract void success(BlockBreakEvent e);

  public abstract boolean predicate(BlockBreakEvent e);

}
