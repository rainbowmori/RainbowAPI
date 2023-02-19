package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.rainbowmori.rainbowapi.object.command.wrappers.IntegerRange;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.RangeArgument;

public class IntegerRangeArgument extends SafeOverrideableArgument<IntegerRange, IntegerRange> {


	public IntegerRangeArgument(String nodeName) {
		super(nodeName, RangeArgument.intRange(), IntegerRange::toString);
	}
	
	@Override
	public Class<IntegerRange> getPrimitiveType() {
		return IntegerRange.class;
	}

	@Override
	public  IntegerRange parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		MinMaxBounds.Ints range = RangeArgument.Ints.getRange(cmdCtx, key);
		int low = range.getMin() == null ? Integer.MIN_VALUE : range.getMin();
		int high = range.getMax() == null ? Integer.MAX_VALUE : range.getMax();
		return new IntegerRange(low, high);
	}
}
