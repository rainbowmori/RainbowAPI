package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.exceptions.BadLiteralException;

import java.util.List;

public class MultiLiteralArgument extends Argument<String> {

	private final String[] literals;

	public MultiLiteralArgument(final String... literals) {
		super(null, null);
		if(literals == null) {
			throw new BadLiteralException(true);
		}
		if(literals.length == 0) {
			throw new BadLiteralException(false);
		}
		this.literals = literals;
	}

	public MultiLiteralArgument(final List<String> literals) {
		this(literals.toArray(String[]::new));
	}

	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	public String[] getLiterals() {
		return literals;
	}

	@Override
	public <CommandListenerWrapper> String parseArgument(
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) {
		throw new IllegalStateException("Cannot parse MultiLiteralArgument");
	}
}
