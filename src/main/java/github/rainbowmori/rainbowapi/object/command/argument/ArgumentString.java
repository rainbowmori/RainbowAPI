package github.rainbowmori.rainbowapi.object.command.argument;

import java.util.Collections;
import java.util.List;

public class ArgumentString extends Argument<ArgumentString> {

    private final String string;

    public ArgumentString(String string) {
        this.string = string;
    }

    @Override
    public List<String> getArg() {
        return Collections.singletonList(string);
    }

    @Override
    public boolean isArgMatch(String matched) {
        return string.equalsIgnoreCase(matched);
    }
}
