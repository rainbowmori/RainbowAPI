/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.rainbowmori.rainbowapi.RMHome;
import github.rainbowmori.rainbowapi.object.command.arguments.Argument;
import github.rainbowmori.rainbowapi.object.command.arguments.ArgumentSuggestions;
import github.rainbowmori.rainbowapi.object.command.arguments.LiteralArgument;
import github.rainbowmori.rainbowapi.object.command.arguments.MultiLiteralArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.permissions.Permission;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;


public class CommandAPIHandler {

	private final static VarHandle COMMANDNODE_CHILDREN;
	private final static VarHandle COMMANDNODE_LITERALS;
	private final static VarHandle COMMANDNODE_ARGUMENTS;
	private final static VarHandle COMMANDCONTEXT_ARGUMENTS;
	private static final MinecraftServer MINECRAFT_SERVER;
	private static final CommandDispatcher<CommandSourceStack> DISPATCHER;
	private static CommandAPIHandler instance;
	static {
		MINECRAFT_SERVER = Bukkit.getServer() instanceof CraftServer craftServer ? craftServer.getServer() : null;
		DISPATCHER = MINECRAFT_SERVER.getCommands().getDispatcher();
		VarHandle commandNodeChildren = null;
		VarHandle commandNodeLiterals = null;
		VarHandle commandNodeArguments = null;
		VarHandle commandContextArguments = null;
		try {
			commandNodeChildren = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup()).findVarHandle(CommandNode.class, "children", Map.class);
			commandNodeLiterals = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup()).findVarHandle(CommandNode.class, "literals", Map.class);
			commandNodeArguments = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup()).findVarHandle(CommandNode.class, "arguments", Map.class);
			commandContextArguments = MethodHandles.privateLookupIn(CommandContext.class, MethodHandles.lookup()).findVarHandle(CommandContext.class, "arguments", Map.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		COMMANDNODE_CHILDREN = commandNodeChildren;
		COMMANDNODE_LITERALS = commandNodeLiterals;
		COMMANDNODE_ARGUMENTS = commandNodeArguments;
		COMMANDCONTEXT_ARGUMENTS = commandContextArguments;
	}
	final TreeMap<String, CommandPermission> PERMISSIONS_TO_FIX = new TreeMap<>();
	final List<RegisteredCommand> registeredCommands;

	private CommandAPIHandler() {
		registeredCommands = new ArrayList<>();
	}

	public static CommandAPIHandler getInstance() {
		if (instance == null) {
			instance = new CommandAPIHandler();
		}
		return instance;
	}

	void unregisterted(String commandName, boolean force) {
		RMHome.getRainbowAPI().mcUtil.logInfo("Unregistering command /" + commandName);

		Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) COMMANDNODE_CHILDREN.get(DISPATCHER.getRoot());

		if (force) {
			for (String key : new HashSet<>(commandNodeChildren.keySet())) {
				if (key.contains(":") && key.split(":")[1].equalsIgnoreCase(commandName)) {
					commandNodeChildren.remove(key);
				}
			}
		}

		commandNodeChildren.remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_LITERALS.get(DISPATCHER.getRoot())).remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_ARGUMENTS.get(DISPATCHER.getRoot())).remove(commandName);
	}

	public static void unregister(String command) {
		CommandAPIHandler.getInstance().unregisterted(command, false);
	}


	public static void unregister(String command, boolean force) {
		CommandAPIHandler.getInstance().unregisterted(command, force);
	}

	Command<net.minecraft.commands.CommandSourceStack> generateCommand(Argument<?>[] args, CommandAPIExecutor<? extends CommandSender> executor) {

		return cmdCtx ->
			executor.execute(cmdCtx.getSource().getBukkitSender(), CommandAPIHandler.this.argsToObjectArr(cmdCtx, args));
	}

	Object[] argsToObjectArr(CommandContext<CommandSourceStack> cmdCtx, Argument<?>[] args) throws CommandSyntaxException {
		List<Object> argList = new ArrayList<>();
		for (Argument<?> argument : args) {
			if (argument.isListed()) {
				argList.add(parseArgument(cmdCtx, argument.getNodeName(), argument, argList.toArray()));
			}
		}

		return argList.toArray();
	}

	Object parseArgument(CommandContext<CommandSourceStack> cmdCtx, String key, Argument<?> value, Object[] previousArgs) throws CommandSyntaxException {
		if (value.isListed()) {
			return value.parseArgument(cmdCtx, key, previousArgs);
		} else {
			return null;
		}
	}

	Predicate<CommandSourceStack> generatePermissions(String commandName, CommandPermission permission, Predicate<CommandSender> requirements) {
		if (PERMISSIONS_TO_FIX.containsKey(commandName.toLowerCase())) {
			if (!PERMISSIONS_TO_FIX.get(commandName.toLowerCase()).equals(permission)) {
				permission = PERMISSIONS_TO_FIX.get(commandName.toLowerCase());
			}
		} else {
			PERMISSIONS_TO_FIX.put(commandName.toLowerCase(), permission);
		}

		final CommandPermission finalPermission = permission;

		if (finalPermission.getPermission().isPresent()) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(finalPermission.getPermission().get()));
			} catch (IllegalArgumentException e) {
				assert true;
			}
		}

		return (CommandSourceStack css) -> permissionCheck(css.getBukkitSender(), finalPermission, requirements);
	}

	boolean permissionCheck(CommandSender sender, CommandPermission permission, Predicate<CommandSender> requirements) {
		boolean satisfiesPermissions;
		if (sender == null) {
			satisfiesPermissions = true;
		} else {
			if (permission.equals(CommandPermission.NONE)) {
				// No permission set
				satisfiesPermissions = true;
			} else if (permission.equals(CommandPermission.OP)) {
				// Op permission set
				satisfiesPermissions = sender.isOp();
			} else {
				// A permission has been set
				if (permission.getPermission().isPresent()) {
					satisfiesPermissions = sender.hasPermission(permission.getPermission().get());
				} else {
					satisfiesPermissions = true;
				}
			}
		}
		if (permission.isNegated()) {
			satisfiesPermissions = !satisfiesPermissions;
		}
		return satisfiesPermissions && requirements.test(sender);
	}

	private boolean expandMultiLiterals(CommandMetaData meta, final Argument<?>[] args, CommandAPIExecutor<? extends CommandSender> executor) throws CommandSyntaxException, IOException {

		// "Expands" our MultiLiterals into Literals
		for (int index = 0; index < args.length; index++) {
			// Find the first multiLiteral in the for loop
			if (args[index] instanceof MultiLiteralArgument superArg) {

				// Add all of its entries
				for (int i = 0; i < superArg.getLiterals().length; i++) {
					LiteralArgument litArg = (LiteralArgument) new LiteralArgument(superArg.getLiterals()[i]).setListed(superArg.isListed()).withPermission(superArg.getArgumentPermission()).withRequirement(superArg.getRequirements());

					// Reconstruct the list of arguments and place in the new literals
					Argument<?>[] newArgs = Arrays.copyOf(args, args.length);
					newArgs[index] = litArg;
					register(meta, newArgs, executor);
				}
				return true;
			}
		}
		return false;
	}

	private boolean hasCommandConflict(String commandName, Argument<?>[] args, String argumentsAsString) {
		List<String[]> regArgs = new ArrayList<>();
		for (RegisteredCommand rCommand : registeredCommands) {
			if (rCommand.commandName().equals(commandName)) {
				for (String str : rCommand.argsAsStr()) {
					regArgs.add(str.split(":"));
				}
				break;
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (regArgs.size() == i) {
				break;
			}
			// We want to ensure all node names are the same
			if (!regArgs.get(i)[0].equals(args[i].getNodeName())) {
				break;
			}
			// This only applies to the last argument
			if (i == args.length - 1) {
				if (!regArgs.get(i)[1].equals(args[i].getClass().getSimpleName())) {
					// Command it conflicts with
					StringBuilder builder2 = new StringBuilder();
					for (String[] arg : regArgs) {
						builder2.append(arg[0]).append("<").append(arg[1]).append("> ");
					}

					RMHome.getRainbowAPI().mcUtil.logError("""
						Failed to register command:

						  %s %s

						Because it conflicts with this previously registered command:

						  %s %s
						""".formatted(commandName, argumentsAsString, commandName, builder2.toString()));
					return true;
				}
			}
		}
		return false;
	}

	// Links arg -> Executor
	private ArgumentBuilder<CommandSourceStack, ?> generateInnerArgument(Command<CommandSourceStack> command, Argument<?>[] args) {
		Argument<?> innerArg = args[args.length - 1];

		if (innerArg instanceof LiteralArgument literalArgument) {
			return getLiteralArgumentBuilderArgument(literalArgument.getLiteral(), innerArg.getArgumentPermission(), innerArg.getRequirements()).executes(command);
		} else {
			return getRequiredArgumentBuilderDynamic(args, innerArg).executes(command);
		}
	}

	private ArgumentBuilder<CommandSourceStack, ?> generateOuterArguments(ArgumentBuilder<CommandSourceStack, ?> innermostArgument, Argument<?>[] args) {
		ArgumentBuilder<CommandSourceStack, ?> outer = innermostArgument;
		for (int i = args.length - 2; i >= 0; i--) {
			Argument<?> outerArg = args[i];

			if (outerArg instanceof LiteralArgument literalArgument) {
				outer = getLiteralArgumentBuilderArgument(literalArgument.getLiteral(), outerArg.getArgumentPermission(), outerArg.getRequirements()).then(outer);
			} else {
				outer = getRequiredArgumentBuilderDynamic(args, outerArg).then(outer);
			}
		}
		return outer;
	}

	void register(CommandMetaData meta, final Argument<?>[] args, CommandAPIExecutor<? extends CommandSender> executor) throws CommandSyntaxException, IOException {
		if (expandMultiLiterals(meta, args, executor)) {
			return;
		}

		final String humanReadableCommandArgSyntax;
		{
			StringBuilder builder = new StringBuilder();
			for (Argument<?> arg : args) {
				builder.append(arg.toString()).append(" ");
			}
			humanReadableCommandArgSyntax = builder.toString().trim();
		}

		{
			Set<String> argumentNames = new HashSet<>();
			for (Argument<?> arg : args) {
				if (!(arg instanceof LiteralArgument)) {
					if (argumentNames.contains(arg.getNodeName())) {
						RMHome.getRainbowAPI().mcUtil.logError("""
							Failed to register command:

							  %s %s

							Because the following argument shares the same node name as another argument:

							  %s
							""".formatted(meta.commandName, humanReadableCommandArgSyntax, arg.toString()));
						return;
					} else {
						argumentNames.add(arg.getNodeName());
					}
				}
			}
		}

		String commandName = meta.commandName;
		CommandPermission permission = meta.permission;
		String[] aliases = meta.aliases;
		Predicate<CommandSender> requirements = meta.requirements;
		Optional<String> shortDescription = meta.shortDescription;
		Optional<String> fullDescription = meta.fullDescription;

		boolean hasRegisteredCommand = false;
		for (int i = 0, size = registeredCommands.size(); i < size && !hasRegisteredCommand; i++) {
			hasRegisteredCommand = registeredCommands.get(i).commandName().equals(commandName);
		}
		if (hasRegisteredCommand && hasCommandConflict(commandName, args, humanReadableCommandArgSyntax)) {
			return;
		} else {
			List<String> argumentsString = new ArrayList<>();
			for (Argument<?> arg : args) {
				argumentsString.add(arg.getNodeName() + ":" + arg.getClass().getSimpleName());
			}
			registeredCommands.add(new RegisteredCommand(commandName, argumentsString, shortDescription, fullDescription, aliases, permission));
		}
		{
			final PluginCommand pluginCommand = Bukkit.getPluginCommand(commandName);
			if (pluginCommand != null) {
				RMHome.getRainbowAPI().mcUtil.logWarning("Plugin command /%s is registered by Bukkit (%s). Did you forget to remove this from your plugin.yml file?".formatted(commandName, pluginCommand.getPlugin().getName()));
			}
		}

		RMHome.getRainbowAPI().mcUtil.logInfo("Registering command /" + commandName + " " + humanReadableCommandArgSyntax);

		Command<CommandSourceStack> command = generateCommand(args, executor);

		LiteralCommandNode<CommandSourceStack> resultantNode;
		if (args.length == 0) {
			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName).requires(generatePermissions(commandName, permission, requirements)).executes(command));

			for (String alias : aliases) {
				RMHome.getRainbowAPI().mcUtil.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());
				DISPATCHER.register(getLiteralArgumentBuilder(alias).requires(generatePermissions(alias, permission, requirements)).executes(command));
			}
		} else {
			ArgumentBuilder<CommandSourceStack, ?> commandArguments = generateOuterArguments(generateInnerArgument(command, args), args);

			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName).requires(generatePermissions(commandName, permission, requirements)).then(commandArguments));

			for (String alias : aliases) {

				RMHome.getRainbowAPI().mcUtil.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());

				DISPATCHER.register(getLiteralArgumentBuilder(alias).requires(generatePermissions(alias, permission, requirements)).then(commandArguments));
			}
		}
	}

	LiteralArgumentBuilder<CommandSourceStack> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}

	LiteralArgumentBuilder<CommandSourceStack> getLiteralArgumentBuilderArgument(String commandName, CommandPermission permission, Predicate<CommandSender> requirements) {
		LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.literal(commandName);
		return builder.requires((CommandSourceStack css) -> permissionCheck(css.getBukkitSender(), permission, requirements));
	}

	RequiredArgumentBuilder<CommandSourceStack, ?> getRequiredArgumentBuilderDynamic(final Argument<?>[] args, Argument<?> argument) {

		final SuggestionProvider<CommandSourceStack> suggestions;

		if (argument.getOverriddenSuggestions().isPresent()) {
			suggestions = toSuggestions(argument, args, true);
		} else if (argument.getIncludedSuggestions().isPresent()) {
			suggestions = (cmdCtx, builder) -> argument.getRawType().listSuggestions(cmdCtx, builder);
		} else {
			suggestions = null;
		}

		return getRequiredArgumentBuilderWithProvider(argument, args, suggestions);
	}

	RequiredArgumentBuilder<CommandSourceStack, ?> getRequiredArgumentBuilderWithProvider(Argument<?> argument, Argument<?>[] args, SuggestionProvider<CommandSourceStack> provider) {
		SuggestionProvider<CommandSourceStack> newSuggestionsProvider = provider;

		if (argument.getIncludedSuggestions().isPresent() && argument.getOverriddenSuggestions().isEmpty()) {
			SuggestionProvider<CommandSourceStack> addedSuggestions = toSuggestions(argument, args, false);

			newSuggestionsProvider = (cmdCtx, builder) -> {

				CompletableFuture<Suggestions> addedSuggestionsFuture = addedSuggestions.getSuggestions(cmdCtx, builder);
				CompletableFuture<Suggestions> providerSuggestionsFuture = provider.getSuggestions(cmdCtx, builder);
				CompletableFuture<Suggestions> result = new CompletableFuture<>();
				CompletableFuture.allOf(addedSuggestionsFuture, providerSuggestionsFuture).thenRun(() -> {
					List<Suggestions> suggestions = new ArrayList<>();
					suggestions.add(addedSuggestionsFuture.join());
					suggestions.add(providerSuggestionsFuture.join());
					result.complete(Suggestions.merge(cmdCtx.getInput(), suggestions));
				});
				return result;
			};
		}

		RequiredArgumentBuilder<CommandSourceStack, ?> requiredArgumentBuilder = RequiredArgumentBuilder.argument(argument.getNodeName(), argument.getRawType());

		return requiredArgumentBuilder.requires(css -> permissionCheck(css.getBukkitSender(), argument.getArgumentPermission(), argument.getRequirements())).suggests(newSuggestionsProvider);
	}

	Object[] generatePreviousArguments(CommandContext<CommandSourceStack> context, Argument<?>[] args, String nodeName) throws CommandSyntaxException {
		List<Object> previousArguments = new ArrayList<>();

		for (Argument<?> arg : args) {
			if (arg.getNodeName().equals(nodeName) && !(arg instanceof LiteralArgument)) {
				break;
			}

			Object result;
			try {
				result = parseArgument(context, arg.getNodeName(), arg, previousArguments.toArray());
			} catch (IllegalArgumentException e) {

				result = null;
			}
			if (arg.isListed()) {
				previousArguments.add(result);
			}
		}
		return previousArguments.toArray();
	}

	SuggestionProvider<CommandSourceStack> toSuggestions(Argument<?> theArgument, Argument<?>[] args, boolean overrideSuggestions) {
		return (CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) -> {
			SuggestionInfo suggestionInfo = new SuggestionInfo(context.getSource().getBukkitSender(), generatePreviousArguments(context, args, theArgument.getNodeName()), builder.getInput(), builder.getRemaining());
			Optional<ArgumentSuggestions> suggestionsToAddOrOverride = overrideSuggestions ? theArgument.getOverriddenSuggestions() : theArgument.getIncludedSuggestions();
			return suggestionsToAddOrOverride.orElse(ArgumentSuggestions.empty()).suggest(suggestionInfo, builder);
		};
	}


	private static final class CartesianProduct {

		private CartesianProduct() {
		}

		public static <T> List<List<T>> getDescartes(List<List<T>> list) {
			List<List<T>> returnList = new ArrayList<>();
			descartesRecursive(list, 0, returnList, new ArrayList<>());
			return returnList;
		}

		private static <T> void descartesRecursive(List<List<T>> originalList, int position, List<List<T>> returnList, List<T> cacheList) {
			List<T> originalItemList = originalList.get(position);
			for (int i = 0; i < originalItemList.size(); i++) {
				// The last one reuses cacheList to save memory
				List<T> childCacheList = (i == originalItemList.size() - 1) ? cacheList : new ArrayList<>(cacheList);
				childCacheList.add(originalItemList.get(i));
				if (position == originalList.size() - 1) {// Exit recursion to the end
					returnList.add(childCacheList);
					continue;
				}
				descartesRecursive(originalList, position + 1, returnList, childCacheList);
			}
		}

	}

}
