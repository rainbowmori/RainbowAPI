
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


	public final Argument<T> includeSafeSuggestions(SafeSuggestions<S> suggestions) {
		return this.includeSuggestions(suggestions.toSuggestions(mapper));
	}

	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	@Deprecated(forRemoval = true)
	public final Argument<T> includeWithSafeSuggestionsT(Function<SuggestionInfo, Tooltip<S>[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

	static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}

}
