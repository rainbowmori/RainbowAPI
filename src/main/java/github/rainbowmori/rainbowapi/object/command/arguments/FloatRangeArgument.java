package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.wrappers.FloatRange;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.RangeArgument;

public class FloatRangeArgument extends SafeOverrideableArgument<FloatRange, FloatRange> {


	public FloatRangeArgument(String nodeName) {
		super(nodeName, RangeArgument.floatRange(), FloatRange::toString);
	}

	@Override
	public Class<FloatRange> getPrimitiveType() {
		return FloatRange.class;
	}

	@Override
	public  FloatRange parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		MinMaxBounds.Doubles range = RangeArgument.Floats.getRange(cmdCtx, key);
		double low = range.getMin() == null ? -Float.MAX_VALUE : range.getMin();
		double high = range.getMax() == null ? Float.MAX_VALUE : range.getMax();
		return new FloatRange((float) low, (float) high);
	}
}
