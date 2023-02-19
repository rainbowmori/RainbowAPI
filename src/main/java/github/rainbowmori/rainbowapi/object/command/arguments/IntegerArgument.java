
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.exceptions.InvalidRangeException;
import net.minecraft.commands.CommandSourceStack;

public class IntegerArgument extends SafeOverrideableArgument<Integer, Integer> {

	/**
	 * An integer argument
	 * @param nodeName the name of the node for this argument
	 */
	public IntegerArgument(String nodeName) {
		super(nodeName, IntegerArgumentType.integer(), String::valueOf);
	}
	
	/**
	 * An integer argument with a minimum value
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(String nodeName, int min) {
		super(nodeName, IntegerArgumentType.integer(min), String::valueOf);
	}
	
	/**
	 * An integer argument with a minimum and maximum value
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(String nodeName, int min, int max) {
		super(nodeName, IntegerArgumentType.integer(min, max), String::valueOf);
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<Integer> getPrimitiveType() {
		return int.class;
	}

	@Override
	public Integer parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
	
}
