package github.rainbowmori.rainbowapi.object.playerinput;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class SingleInput extends ValidatingPrompt {

    private final String message;

    private final BiFunction<ConversationContext, String, Boolean> isValid;

    private final BiFunction<ConversationContext, String, Prompt> function;

    public SingleInput(String message, BiFunction<ConversationContext, String, Boolean> isValid,
                       BiFunction<ConversationContext, String, Prompt> function) {
        this.message = message;
        this.isValid = isValid;
        this.function = function;
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return isValid.apply(context,input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return function.apply(context, input);
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return message;
    }
}
