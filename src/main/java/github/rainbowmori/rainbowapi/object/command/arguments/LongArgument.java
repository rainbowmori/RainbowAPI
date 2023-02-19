
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.exceptions.InvalidRangeException;
import net.minecraft.commands.CommandSourceStack;

public class LongArgument extends SafeOverrideableArgument<Long, Long> {


	public LongArgument(String nodeName) {
		super(nodeName, LongArgumentType.longArg(), String::valueOf);
	}
	

	public LongArgument(String nodeName, long value) {
		super(nodeName, LongArgumentType.longArg(value), String::valueOf);
	}
	

	public LongArgument(String nodeName, long min, long max) {
		super(nodeName, LongArgumentType.longArg(min, max), String::valueOf);
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<Long> getPrimitiveType() {
		return long.class;
	}

	@Override
	public  Long parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
	
}
