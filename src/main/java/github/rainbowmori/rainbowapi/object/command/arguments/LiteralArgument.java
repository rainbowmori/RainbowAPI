package github.rainbowmori.rainbowapi.object.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import github.rainbowmori.rainbowapi.object.command.exceptions.BadLiteralException;

public class LiteralArgument extends Argument<String> {

    private final String literal;

    public LiteralArgument(final String literal) {

        super(literal, null);

        if (literal == null) {
            throw new BadLiteralException(true);
        }
        if (literal.isEmpty()) {
            throw new BadLiteralException(false);
        }
        this.literal = literal;
        this.setListed(false);
    }

    public static LiteralArgument of(final String literal) {
        return new LiteralArgument(literal);
    }

    public static LiteralArgument literal(final String literal) {
        return new LiteralArgument(literal);
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    public String getLiteral() {
        return literal;
    }


    @Override
    public <CommandListenerWrapper> String parseArgument(
        CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) {
        return literal;
    }
}
