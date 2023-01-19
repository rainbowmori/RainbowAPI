package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.object.RMData;
import github.rainbowmori.rainbowapi.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MultiInput extends PlayerInput {

    private final Map<Integer, Consumer<String>> predicates;

    private final Consumer<List<String>> consumer;


    public MultiInput(@NotNull RMData rmData, int getInput, boolean cancelable, Consumer<List<String>> consumer) {
        super(rmData, getInput, cancelable);
        this.consumer = consumer;
        this.predicates = new HashMap<>();
    }

    public MultiInput(@NotNull RMData rmData, int getInput, boolean cancelable,
                      @NotNull Consumer<List<String>> consumer,@NotNull String[] predicates) {
        super(rmData, getInput, cancelable);
        this.consumer = consumer;
        this.predicates = new HashMap<>() {{
            for (int i = 0; i < getInput; i++) {
                var str = predicates[i];
                if (str != null) put(i, s -> Util.send(rmData.getPlayer(), str));
            }
        }};
    }

    public MultiInput(@NotNull RMData rmData, int getInput, boolean cancelable,
                     @NotNull Consumer<List<String>> consumer, @NotNull Map<Integer, Consumer<String>> predicates) {
        super(rmData, getInput, cancelable);
        this.consumer = consumer;
        this.predicates = predicates;
    }

    @Override
    public void get(String text) {
        texts.add(text);
        Optional.ofNullable(predicates.get(texts.size())).ifPresent(c -> c.accept(text));
        if (texts.size() == getInput) consumer.accept(texts);
    }
}
