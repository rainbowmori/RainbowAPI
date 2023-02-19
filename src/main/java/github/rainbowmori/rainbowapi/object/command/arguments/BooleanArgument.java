
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class BooleanArgument extends SafeOverrideableArgument<Boolean, Boolean> {

	public BooleanArgument(String nodeName) {
		super(nodeName, BoolArgumentType.bool(), String::valueOf);
	}

	@Override
	public Class<Boolean> getPrimitiveType() {
		return boolean.class;
	}

	@Override
	public  Boolean parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs)
			throws CommandSyntaxException {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}

}
