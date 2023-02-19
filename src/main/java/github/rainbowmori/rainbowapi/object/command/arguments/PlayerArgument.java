package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class PlayerArgument extends SafeOverrideableArgument<Player, Player> {

	public PlayerArgument(String nodeName) {
		super(nodeName, GameProfileArgument.gameProfile(), Player::getName);
	}

	@Override
	public Class<Player> getPrimitiveType() {
		return Player.class;
	}

	@Override
	public  Player parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return Bukkit.getPlayer(GameProfileArgument.getGameProfiles(cmdCtx, key).iterator().next().getId());
	}
}
