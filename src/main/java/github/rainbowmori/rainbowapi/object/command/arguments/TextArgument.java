package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class TextArgument extends Argument<String> {

	public TextArgument(String nodeName) {
		super(nodeName, StringArgumentType.string());
	}

	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public  String parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
}
