package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.playerinput.PlayerBlockInput;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class BlockDamage implements Listener {

    private static final BlockDamage INSTANCE = new BlockDamage();

    public static BlockDamage getInstance() {
        return INSTANCE;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void damage(BlockBreakEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (PlayerBlockInput.inputMap.containsKey(uuid)) {
            PlayerBlockInput.inputMap.get(uuid).build(e);
        }
    }
}
