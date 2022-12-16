package github.rainbowmori.rainbowapi.object;

import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerData {
    public static final Map<Player, PlayerData> playerData = new HashMap<>();

    public static PlayerData getData(Player player) {
        return playerData.get(player);
    }

    private final Player player;

    public PlayerData(Player player) {
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
