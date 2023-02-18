
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import github.rainbowmori.rainbowapi.object.command.SuggestionInfo;
import github.rainbowmori.rainbowapi.object.command.Tooltip;
import org.bukkit.NamespacedKey;

import java.util.function.Function;

public abstract class SafeOverrideableArgument<T, S> extends Argument<T> {

	private final Function<S, String> mapper;

	protected SafeOverrideableArgument(String nodeName, ArgumentType<?> rawType, Function<S, String> mapper) {
		super(nodeName, rawType);
		this.mapper = mapper;
	}


	public final Argument<T> replaceSafeSuggestions(SafeSuggestions<S> suggestions) {
		replaceSuggestions(suggestions.toSuggestions(mapper));
		return this;
	}

	@Deprecated(forRemoval = true)
	public final Argument<T> replaceWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}


	@Deprecated(forRemoval = true)
	public final Argument<T> replaceWithSafeSuggestionsT(Function<SuggestionInfo, Tooltip<S>[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

	/**
	 * Includes the suggestions provided with the existing suggestions for this
	 * argument. Use the static methods in {@link SafeSuggestions} to create safe
	 * suggestions.
	 * 
	 * @param suggestions The safe suggestions to use
	 * @return the current argument
	 */
	public final Argument<T> includeSafeSuggestions(SafeSuggestions<S> suggestions) {
		return this.includeSuggestions(suggestions.toSuggestions(mapper));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns a {@link S} array of
	 *                    suggestions to add, where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns a {@link Tooltip}
	 *                    array of suggestions to add, parameterized over {@link S}
	 *                    where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 * 
	 */
	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestionsT(Function<SuggestionInfo, Tooltip<S>[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

	/**
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to
	 * convert <code>S</code> to a <code>String</code>
	 * 
	 * @param mapper the mapping function from <code>S</code> to
	 *               <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to
	 *         <code>String</code>
	 */
	static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}

}
