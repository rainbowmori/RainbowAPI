package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;


public class StringArgument extends Argument<String> {

	public StringArgument(String nodeName) {
		super(nodeName, StringArgumentType.word());
	}

	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public <CommandListenerWrapper> String parseArgument(
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
}
