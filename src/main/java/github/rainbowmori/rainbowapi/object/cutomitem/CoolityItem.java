package github.rainbowmori.rainbowapi.object.cutomitem;

import github.rainbowmori.rainbowapi.object.cutomitem.cooldown.CooldownItem;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link CooldownItem} {@link DurabilityItem} を合わせて、ActionBarMessageを合わせたものです
 */
public interface CoolityItem extends CooldownItem, DurabilityItem {

  @Override
  @NotNull
  default Optional<String> getActionBarMessage(Player player) {
    UUID uuid = player.getUniqueId();
    return Optional.of(getCooldownMessage(uuid) + " " + getDurabilityMessage(uuid));
  }
}
