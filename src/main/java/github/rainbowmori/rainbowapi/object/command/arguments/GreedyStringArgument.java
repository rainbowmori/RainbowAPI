package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class GreedyStringArgument extends SafeOverrideableArgument<String, String> implements IGreedyArgument {

	public GreedyStringArgument(String nodeName) {
		super(nodeName, StringArgumentType.greedyString(), s -> s);
	}

	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public  String parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
}
