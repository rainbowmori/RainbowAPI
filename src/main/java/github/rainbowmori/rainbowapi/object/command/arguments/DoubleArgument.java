package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.exceptions.InvalidRangeException;
import net.minecraft.commands.CommandSourceStack;

public class DoubleArgument extends SafeOverrideableArgument<Double, Double> {

	public DoubleArgument(String nodeName) {
		super(nodeName, DoubleArgumentType.doubleArg(), String::valueOf);
	}

	public DoubleArgument(String nodeName, double min) {
		super(nodeName, DoubleArgumentType.doubleArg(min), String::valueOf);
	}

	public DoubleArgument(String nodeName, double min, double max) {
		super(nodeName, DoubleArgumentType.doubleArg(min, max), String::valueOf);
		if (max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<Double> getPrimitiveType() {
		return double.class;
	}

	@Override
	public  Double parseArgument(
		CommandContext<CommandSourceStack>cmdCtx, String key, Object[] previousArgs)
			throws CommandSyntaxException {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}

}
