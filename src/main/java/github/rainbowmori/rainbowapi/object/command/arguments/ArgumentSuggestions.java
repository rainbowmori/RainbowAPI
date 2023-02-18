package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import github.rainbowmori.rainbowapi.object.command.IStringTooltip;
import github.rainbowmori.rainbowapi.object.command.SuggestionInfo;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@FunctionalInterface
public interface ArgumentSuggestions {

    CompletableFuture<Suggestions> suggest(SuggestionInfo info, SuggestionsBuilder builder)
        throws CommandSyntaxException;
	static ArgumentSuggestions empty() {
		return (info, builder) -> builder.buildFuture();
	}

	static ArgumentSuggestions strings(String... suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions));
	}

	static ArgumentSuggestions strings(Collection<String> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions));
	}

	static ArgumentSuggestions strings(Function<SuggestionInfo, String[]> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions.apply(info)));
	}
	static ArgumentSuggestions stringCollection(Function<SuggestionInfo, Collection<String>> suggestions) {
		return (info, builder) -> future(suggestionsFromStrings(builder, suggestions.apply(info)));
	}

	static ArgumentSuggestions stringsAsync(Function<SuggestionInfo, CompletableFuture<String[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(strings -> suggestionsFromStrings(builder, strings));
	}

	static ArgumentSuggestions stringCollectionAsync(Function<SuggestionInfo, CompletableFuture<Collection<String>>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(strings -> suggestionsFromStrings(builder, strings));
	}

	static ArgumentSuggestions stringsWithTooltips(IStringTooltip... suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions));
	}

	static ArgumentSuggestions stringsWithTooltips(Collection<IStringTooltip> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions));
	}

	static ArgumentSuggestions stringsWithTooltips(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions.apply(info)));
	}
	static ArgumentSuggestions stringsWithTooltipsCollection(Function<SuggestionInfo, Collection<IStringTooltip>> suggestions) {
		return (info, builder) -> future(suggestionsFromTooltips(builder, suggestions.apply(info)));
	}

	static ArgumentSuggestions stringsWithTooltipsAsync(Function<SuggestionInfo, CompletableFuture<IStringTooltip[]>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(stringsWithTooltips -> suggestionsFromTooltips(builder, stringsWithTooltips));
	}

	static ArgumentSuggestions stringsWithTooltipsCollectionAsync(Function<SuggestionInfo, CompletableFuture<Collection<IStringTooltip>>> suggestions) {
		return (info, builder) -> suggestions
			.apply(info)
			.thenApply(stringsWithTooltips -> suggestionsFromTooltips(builder, stringsWithTooltips));
	}

	static ArgumentSuggestions merge(ArgumentSuggestions... suggestions) {
		return (info, builder) -> {
			for(ArgumentSuggestions suggestion : suggestions) {
				suggestion.suggest(info, builder);
			}
			return builder.buildFuture();
		};
	}

	private static Suggestions suggestionsFromStrings(SuggestionsBuilder builder, String... suggestions) {
		for(String suggestion : suggestions) {
			if(shouldSuggest(builder, suggestion)) {
				builder.suggest(suggestion);
			}
		}
		return builder.build();
	}

	private static Suggestions suggestionsFromStrings(SuggestionsBuilder builder, Collection<String> suggestions) {
		for(String suggestion : suggestions) {
			if(shouldSuggest(builder, suggestion)) {
				builder.suggest(suggestion);
			}
		}
		return builder.build();
	}
	private static Suggestions suggestionsFromTooltips(SuggestionsBuilder builder, IStringTooltip... suggestions) {
		for(IStringTooltip suggestion : suggestions) {
			processSuggestion(builder, suggestion);
		}
		return builder.build();
	}

	private static Suggestions suggestionsFromTooltips(SuggestionsBuilder builder, Collection<IStringTooltip> suggestions) {
		for(IStringTooltip suggestion : suggestions) {
			processSuggestion(builder, suggestion);
		}
		return builder.build();
	}

	private static void processSuggestion(SuggestionsBuilder builder, IStringTooltip suggestion) {
		if(!shouldSuggest(builder, suggestion.getSuggestion())) {
			return;
		}

		if(suggestion.getTooltip() == null) {
			builder.suggest(suggestion.getSuggestion());
		} else {
			builder.suggest(suggestion.getSuggestion(), suggestion.getTooltip());
		}
	}

	private static boolean shouldSuggest(SuggestionsBuilder builder, String suggestion) {
		return suggestion.toLowerCase(Locale.ROOT).startsWith(builder.getRemaining().toLowerCase(Locale.ROOT));
	}

	private static <T> CompletableFuture<T> future(T value) {
		return CompletableFuture.completedFuture(value);
	}

}
