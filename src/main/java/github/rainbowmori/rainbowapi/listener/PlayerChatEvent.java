package github.rainbowmori.rainbowapi.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatEvent implements Listener {

    private static final PlayerChatEvent INSTANCE = new PlayerChatEvent();

    public static PlayerChatEvent getINSTANCE() {
        return INSTANCE;
    }

    @EventHandler
    public void Chat(AsyncChatEvent e) {
        Player p = e.getPlayer();

    }
}
