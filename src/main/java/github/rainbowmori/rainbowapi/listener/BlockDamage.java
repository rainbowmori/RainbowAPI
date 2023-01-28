package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.playerinput.PlayerBlockInput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamage implements Listener {

    private static final BlockDamage INSTANCE = new BlockDamage();

    public static BlockDamage getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void damage(BlockDamageEvent e) {
        Player p = e.getPlayer();
        if (PlayerBlockInput.inputMap.containsKey(p)) PlayerBlockInput.inputMap.get(p).build(e);
    }
}
