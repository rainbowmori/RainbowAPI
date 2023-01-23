package github.rainbowmori.rainbowapi.object;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RMData {
    public static final Map<Player, RMData> playerData = new HashMap<>();

    public static RMData getData(Player player) {
        return playerData.get(player);
    }

    private final Player player;

    public RMData(Player player) {
        this.player = player;
        playerData.put(player, this);
    }

    public Player getPlayer() {
        return player;
    }
}
