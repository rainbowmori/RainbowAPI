package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.object.RMData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SingleInput extends PlayerInput{

    private final Consumer<String> consumer;

    public SingleInput(String message,@NotNull RMData rmData, boolean cancelable,@NotNull Consumer<String> consumer) {
        super(message,rmData, 1, cancelable);
        this.consumer = consumer;
    }

    @Override
    public void get(String text) {
        consumer.accept(text);
        cleared();
    }
}
