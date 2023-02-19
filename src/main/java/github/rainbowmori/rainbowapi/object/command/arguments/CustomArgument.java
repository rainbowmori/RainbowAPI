package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import github.rainbowmori.rainbowapi.object.command.CommandAPIHandler;
import github.rainbowmori.rainbowapi.object.command.NMS;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;

public class CustomArgument<T, B> extends Argument<T> {

	private final CustomArgumentInfoParser<T, B> infoParser;
	private final Argument<B> base;

	@Deprecated(forRemoval = true)
	public CustomArgument(String nodeName, CustomArgumentInfoParser<T, String> parser) {
		this(nodeName, parser, false);
	}

	@SuppressWarnings("unchecked")
	@Deprecated(forRemoval = true)
	public CustomArgument(String nodeName, CustomArgumentInfoParser<T, String> parser, boolean keyed) {
		super(nodeName, keyed ? StringArgumentType.string()
				: NMS._ArgumentMinecraftKeyRegistered());
		this.base = (Argument<B>) new DummyArgument(nodeName, keyed);
		this.infoParser = (CustomArgumentInfoParser<T, B>) parser;
		CommandAPIHandler.logWarning(
				"Registering CustomArgument " + nodeName + " with legacy registeration method. This may not work!\n"
						+ "Consider using new CustomArgument(Argument, CustomArgumentInfoParser)");
	}

	public CustomArgument(Argument<B> base, CustomArgumentInfoParser<T, B> parser) {
		super(base.getNodeName(), base.getRawType());
		if (base instanceof LiteralArgument || base instanceof MultiLiteralArgument) {
			throw new IllegalArgumentException(base.getClass().getSimpleName() + " is not a suitable base argument type for a CustomArgument");
		}
		this.base = base;
		this.infoParser = parser;
	}

	@Override
	public Class<T> getPrimitiveType() {
		return null;
	}

	@Override
	public  T parseArgument(
		CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs)
			throws CommandSyntaxException {
		// Get the raw input and parsed input
		final String customResult = CommandAPIHandler.getRawArgumentInput(cmdCtx, key);
		final B parsedInput = base.parseArgument(cmdCtx, key, previousArgs);

		try {
			return infoParser.apply(new CustomArgumentInfo<>(
				cmdCtx.getSource().getBukkitSender(), previousArgs, customResult, parsedInput));
		} catch (CustomArgumentException e) {
			throw e.toCommandSyntax(customResult, cmdCtx);
		} catch (Exception e) {
			String errorMsg = new MessageBuilder("Error in executing command ").appendFullInput().append(" - ")
					.appendArgInput().appendHere().toString().replace("%input%", customResult)
					.replace("%finput%", cmdCtx.getInput());
			throw new SimpleCommandExceptionType(() -> errorMsg).create();
		}
	}

	public static class MessageBuilder {
		StringBuilder builder;
		public MessageBuilder() {
			builder = new StringBuilder();
		}

		public MessageBuilder(String str) {
			builder = new StringBuilder(str);
		}

		public MessageBuilder appendArgInput() {
			builder.append("%input%");
			return this;
		}

		public MessageBuilder appendFullInput() {
			builder.append("%finput%");
			return this;
		}

		public MessageBuilder appendHere() {
			builder.append("<--[HERE]");
			return this;
		}

		public MessageBuilder append(String str) {
			builder.append(str);
			return this;
		}


		public MessageBuilder append(Object obj) {
			builder.append(obj);
			return this;
		}

		@Override
		public String toString() {
			return builder.toString();
		}
	}


	@SuppressWarnings("serial")
	public static class CustomArgumentException extends Exception {

		private final String errorMessage;
		private final MessageBuilder errorMessageBuilder;


		public CustomArgumentException(String errorMessage) {
			this.errorMessage = errorMessage;
			this.errorMessageBuilder = null;
		}


		public CustomArgumentException(MessageBuilder errorMessage) {
			this.errorMessage = null;
			this.errorMessageBuilder = errorMessage;
		}

		public CommandSyntaxException toCommandSyntax(String result, CommandContext<?> cmdCtx) {
			if (errorMessage == null) {
				String errorMsg = errorMessageBuilder.toString().replace("%input%", result).replace("%finput%",
						cmdCtx.getInput());
				return new SimpleCommandExceptionType(() -> errorMsg).create();
			} else {
				return new SimpleCommandExceptionType(new LiteralMessage(errorMessage)).create();
			}
		}

	}


	public record CustomArgumentInfo<B> (

			CommandSender sender,

			Object[] previousArgs,


			String input,


			B currentInput) {
	}


	@FunctionalInterface
	public interface CustomArgumentInfoParser<T, B> {


		T apply(CustomArgumentInfo<B> info) throws CustomArgumentException;
	}

	@Deprecated
	private static class DummyArgument extends Argument<String> {

		private final boolean keyed;

		private DummyArgument(String nodeName, boolean keyed) {
			super(nodeName, keyed ? StringArgumentType.string()
					: NMS._ArgumentMinecraftKeyRegistered());
			this.keyed = keyed;
		}

		@Override
		public Class<String> getPrimitiveType() {
			return String.class;
		}

		@Override
		public  String parseArgument(
				CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs)
				throws CommandSyntaxException {
			return keyed ? NMS.getMinecraftKey(cmdCtx, key).toString() : cmdCtx.getArgument(key, String.class);
		}
	}
}
