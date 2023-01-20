package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicateInput extends SingleInput{

    private final String error;
    private final Predicate<String> predicate;
    public PredicateInput(String message, @NotNull RMData rmData, boolean cancelable, @NotNull Consumer<String> consumer, String error, Predicate<String> predicate) {
        super(message, rmData, cancelable, consumer);
        this.error = error;
        this.predicate = predicate;
    }

    @Override
    public void get(String text) {
        if (!predicate.test(text)) {
            Util.send(data.getPlayer(),"<red>" + error);
            return;
        }
        super.get(text);
    }
}
