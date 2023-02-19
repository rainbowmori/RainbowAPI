
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.exceptions.InvalidRangeException;
import net.minecraft.commands.CommandSourceStack;


public class FloatArgument extends SafeOverrideableArgument<Float, Float> {


	public FloatArgument(String nodeName) {
		super(nodeName, FloatArgumentType.floatArg(), String::valueOf);
	}

	public FloatArgument(String nodeName, float min) {
		super(nodeName, FloatArgumentType.floatArg(min), String::valueOf);
	}
	

	public FloatArgument(String nodeName, float min, float max) {
		super(nodeName, FloatArgumentType.floatArg(min, max), String::valueOf);
		if(max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<Float> getPrimitiveType() {
		return float.class;
	}
	
	@Override
	public  Float parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, getPrimitiveType());
	}
}
