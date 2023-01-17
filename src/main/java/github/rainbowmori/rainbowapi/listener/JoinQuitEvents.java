package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.RMData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvents implements Listener {

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        new RMData(e.getPlayer());
    }

    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        RMData.playerData.remove(e.getPlayer());
    }
}
