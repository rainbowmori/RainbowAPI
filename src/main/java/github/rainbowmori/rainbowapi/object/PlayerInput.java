package github.rainbowmori.rainbowapi.object;

import github.rainbowmori.rainbowapi.util.Util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerInput {

    private final RMData data;
    private final Consumer<List<String>> consumer;
    private final int getTexts;
    private final List<String> texts = new ArrayList<>();

    private final Map<Integer, Predicate<String>> predicates;

    private final boolean cancelable;

    public PlayerInput(RMData data, int getTexts, boolean cancelable, Consumer<List<String>> consumer) {
        this(data, getTexts, cancelable, consumer, new HashMap<>());
    }

    public PlayerInput(RMData data, int getTexts, boolean cancelable,
                       Consumer<List<String>> consumer, Map<Integer, Predicate<String>> predicates) {
        this.data = data;
        this.getTexts = getTexts;
        this.cancelable = cancelable;
        this.consumer = consumer;
        this.predicates = Objects.requireNonNullElse(predicates, new HashMap<>());
    }

    public void add(String str) {
        if (str.equals("cancel")) {
            if(cancelable) {
                Util.send(data.getPlayer(), "<blue>チャット入力をキャンセルしました");
                cleared();
            }else Util.send(data.getPlayer(), "<red>この入力はキャンセルできません");
            return;
        }
        int size = texts.size() + 1;
        var predicate = predicates.get(size);
        if (predicate == null || predicate.test(str)) {
            texts.add(str);
            if (texts.size() == getTexts) consumer();
        }
    }

    public void consumer() {
        consumer.accept(texts);
        cleared();
    }

    public void cleared() {
        data.clearPlayerInput();
    }
}
