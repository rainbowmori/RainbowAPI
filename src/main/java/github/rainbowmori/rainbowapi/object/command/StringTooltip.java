
package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class StringTooltip implements IStringTooltip {

	private final String suggestion;
	private final Message tooltip;


	@Deprecated(forRemoval = true)
	public static StringTooltip of(String suggestion, String tooltip) {
		return ofString(suggestion, tooltip);
	}

	public static StringTooltip ofString(String suggestion, String tooltip) {
		return ofMessage(suggestion, Tooltip.messageFromString(tooltip));
	}

	public static StringTooltip ofMessage(String suggestion, Message tooltip) {
		return tooltip == null ? none(suggestion) : new StringTooltip(suggestion, tooltip);
	}


	public static StringTooltip none(String suggestion) {
		return new StringTooltip(suggestion, null);
	}

	public static Collection<StringTooltip> none(String... suggestions) {
		return generate(String::toString, (s, t) -> StringTooltip.none(s), suggestions);
	}

	public static Collection<StringTooltip> none(Collection<String> suggestions) {
		return generate(String::toString, (s, t) -> StringTooltip.none(s), suggestions);
	}


	public static Collection<StringTooltip> generateStrings(Function<String, String> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofString, suggestions);
	}

	public static Collection<StringTooltip> generateStrings(Function<String, String> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofString, suggestions);
	}


	public static Collection<StringTooltip> generateMessages(Function<String, Message> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofMessage, suggestions);
	}

	public static Collection<StringTooltip> generateMessages(Function<String, Message> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, StringTooltip::ofMessage, suggestions);
	}


	private static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, String... suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, Arrays.stream(suggestions));
	}

	private static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, Collection<String> suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, suggestions.stream());
	}


	private static <T> Collection<StringTooltip> generate(Function<String, T> tooltipGenerator, BiFunction<String, T, StringTooltip> tooltipWrapper, Stream<String> suggestions) {
		Function<String, StringTooltip> builder = suggestion -> tooltipWrapper.apply(suggestion, tooltipGenerator.apply(suggestion));
		return suggestions.map(builder).toList();
	}

	private StringTooltip(String suggestion, Message tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}

	public String getSuggestion() {
		return this.suggestion;
	}
	

	public Message getTooltip() {
		return this.tooltip;
	}
	
}
