package github.rainbowmori.rainbowapi.object;

import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private PlayerInput input;

    public Optional<PlayerInput> getPlayerInput() {
        return Optional.ofNullable(input);
    }

    public boolean setPlayerInput(@NotNull PlayerInput playerInput) {
        if(this.input != null) {
            Util.send(player,"<red>チャット入力がまだ終わっていないのがあります");
            return false;
        }
        this.input = playerInput;
        return true;
    }

    public void clearPlayerInput() {
        this.input = null;
    }
}
