package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.arguments.SafeOverrideableArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import org.bukkit.World;

public class WorldArgument extends SafeOverrideableArgument<World, World> {

	public WorldArgument(String nodeName) {
		super(nodeName, DimensionArgument.dimension(), world -> world.getName().toLowerCase());
	}

	@Override
	public Class<World> getPrimitiveType() {
		return World.class;
	}

	@Override
	public World parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return DimensionArgument.getDimension(cmdCtx, key).getWorld();
	}
}
