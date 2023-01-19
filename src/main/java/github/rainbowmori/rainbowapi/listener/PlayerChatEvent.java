package github.rainbowmori.rainbowapi.listener;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.util.Util;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class PlayerChatEvent implements Listener {

    private static final PlayerChatEvent INSTANCE = new PlayerChatEvent();

    public static PlayerChatEvent getINSTANCE() {
        return INSTANCE;
    }

    @EventHandler
    public void Chat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Optional.ofNullable(RMData.getData(p)).
                flatMap(RMData::getPlayerInput).ifPresent(playerInput -> {
                    playerInput.input(Util.serialize(e.message()));
                    e.setCancelled(true);
                });
    }
}
