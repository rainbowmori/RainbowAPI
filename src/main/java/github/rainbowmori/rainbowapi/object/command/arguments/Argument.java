
package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.ArgumentTree;
import github.rainbowmori.rainbowapi.object.command.CommandPermission;
import github.rainbowmori.rainbowapi.object.command.IStringTooltip;
import github.rainbowmori.rainbowapi.object.command.SuggestionInfo;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Argument<T> extends ArgumentTree {

    private final String nodeName;
    private final ArgumentType<?> rawType;
    private Optional<ArgumentSuggestions> suggestions = Optional.empty();
    private Optional<ArgumentSuggestions> addedSuggestions = Optional.empty();
    private CommandPermission permission = CommandPermission.NONE;
    private Predicate<CommandSender> requirements = s -> true;
    private boolean isListed = true;

    protected Argument(String nodeName, ArgumentType<?> rawType) {
        this.nodeName = nodeName;
        this.rawType = rawType;
    }

    public abstract Class<T> getPrimitiveType();

    public final ArgumentType<?> getRawType() {
        return this.rawType;
    }

    public final String getNodeName() {
        return this.nodeName;
    }

    public abstract <CommandSourceStack> T parseArgument(
        CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs);

    public Argument<T> includeSuggestions(ArgumentSuggestions suggestions) {
        this.addedSuggestions = Optional.of(suggestions);
        return this;
    }

    @Deprecated(forRemoval = true)
    public Argument<T> includeSuggestions(Function<SuggestionInfo, String[]> suggestions) {
        return includeSuggestions(ArgumentSuggestions.strings(suggestions));
    }

    @Deprecated(forRemoval = true)
    public Argument<T> includeSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
        return includeSuggestions(ArgumentSuggestions.stringsWithTooltips(suggestions));
    }

    public Optional<ArgumentSuggestions> getIncludedSuggestions() {
        return addedSuggestions;
    }

    public Argument<T> replaceSuggestions(ArgumentSuggestions suggestions) {
        this.suggestions = Optional.of(suggestions);
        return this;
    }

    @Deprecated(forRemoval = true)
    public Argument<T> replaceSuggestions(Function<SuggestionInfo, String[]> suggestions) {
        return replaceSuggestions(ArgumentSuggestions.strings(suggestions));
    }

    @Deprecated(forRemoval = true)
    public Argument<T> replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
        return replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(suggestions));
    }

    public final Optional<ArgumentSuggestions> getOverriddenSuggestions() {
        return suggestions;
    }

    public final Argument<T> withPermission(CommandPermission permission) {
        this.permission = permission;
        return this;
    }

    public final Argument<T> withPermission(String permission) {
        this.permission = CommandPermission.fromString(permission);
        return this;
    }

    public final CommandPermission getArgumentPermission() {
        return permission;
    }

    public final Predicate<CommandSender> getRequirements() {
        return this.requirements;
    }

    public final Argument<T> withRequirement(Predicate<CommandSender> requirement) {
        this.requirements = this.requirements.and(requirement);
        return this;
    }

    public boolean isListed() {
        return this.isListed;
    }

    public Argument<T> setListed(boolean listed) {
        this.isListed = listed;
        return this;
    }

    public List<String> getEntityNames(Object argument) {
        return Arrays.asList(new String[]{null});
    }

    @Override
    public String toString() {
        return this.getNodeName() + "<" + this.getClass().getSimpleName() + ">";
    }

}