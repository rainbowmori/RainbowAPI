package github.rainbowmori.rainbowapi.object.command.exceptions;

import github.rainbowmori.rainbowapi.object.command.arguments.Argument;


public class GreedyArgumentException extends RuntimeException {


	public GreedyArgumentException(Argument<?>[] arguments) {
		super("List の最後で宣言できる GreedyStringArgument は 1 つだけです。見つかった引数:"
				+ buildArgsStr(arguments));
	}

	private static String buildArgsStr(Argument<?>[] arguments) {
		StringBuilder builder = new StringBuilder();
		for (Argument<?> arg : arguments) {
			builder.append(arg.getNodeName()).append("<").append(arg.getClass().getSimpleName()).append("> ");
		}
		return builder.toString();
	}

}
