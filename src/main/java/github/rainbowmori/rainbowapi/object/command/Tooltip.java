package github.rainbowmori.rainbowapi.object.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;


public class Tooltip<S> {

	private final S object;
	private final Message tooltip;

	protected Tooltip(S object, Message tooltip) {
		this.object = object;
		this.tooltip = tooltip;
	}

	@Deprecated(forRemoval = true)
	public static <S> Tooltip<S> of(S object, String tooltip) {
		return ofString(object, tooltip);
	}

	public static <S> Tooltip<S> ofString(S object, String tooltip) {
		return ofMessage(object, messageFromString(tooltip));
	}

	public static <S> Tooltip<S> ofMessage(S object, Message tooltip) {
		return new Tooltip<>(object, tooltip);
	}

	public static <S> Tooltip<S> none(S object) {
		return new Tooltip<>(object, null);
	}

	@SafeVarargs
	public static <S> Collection<Tooltip<S>> none(S... suggestions) {
		return generate(S::toString, (s, t) -> Tooltip.none(s), suggestions);
	}

	public static <S> Collection<Tooltip<S>> none(Collection<S> suggestions) {
		return generate(S::toString, (s, t) -> Tooltip.none(s), suggestions);

	}

	@SafeVarargs
	public static <S> Collection<Tooltip<S>> generateStrings(Function<S, String> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofString, suggestions);
	}

	public static <S> Collection<Tooltip<S>> generateStrings(Function<S, String> tooltipGenerator, Collection<S> suggestions) {
		return generate(tooltipGenerator, Tooltip::ofString, suggestions);
	}

	@SafeVarargs
	public static <S> Collection<Tooltip<S>> generateMessages(Function<S, Message> tooltipGenerator, S... suggestions) {
		return generate(tooltipGenerator, Tooltip::ofMessage, suggestions);
	}

	public static <S> Collection<Tooltip<S>> generateMessages(Function<S, Message> tooltipGenerator, Collection<S> suggestions) {
		return generate(tooltipGenerator, Tooltip::ofMessage, suggestions);
	}

	@SafeVarargs
	private static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, S... suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, Arrays.stream(suggestions));
	}

	private static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, Collection<S> suggestions) {
		return generate(tooltipGenerator, tooltipWrapper, suggestions.stream());
	}

	private static <S, T> Collection<Tooltip<S>> generate(Function<S, T> tooltipGenerator, BiFunction<S, T, Tooltip<S>> tooltipWrapper, Stream<S> suggestions) {
		return suggestions.map(suggestion -> tooltipWrapper.apply(suggestion, tooltipGenerator.apply(suggestion))).toList();
	}

	@SafeVarargs
	public static <S> Tooltip<S>[] arrayOf(Tooltip<S>... tooltips) {
		return tooltips;
	}

	public static <S> Function<Tooltip<S>, StringTooltip> build(Function<S, String> mapper) {
		return t -> StringTooltip.ofMessage(mapper.apply(t.object), t.tooltip);
	}

	public static Message messageFromString(String string) {
		return new LiteralMessage(string);
	}

	public S getSuggestion() {
		return object;
	}

	public Message getTooltip() {
		return tooltip;
	}

}
