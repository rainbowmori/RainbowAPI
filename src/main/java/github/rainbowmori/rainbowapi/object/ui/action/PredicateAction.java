package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class PredicateAction implements MenuAction {
    private final BiPredicate<MenuHolder<?>, InventoryClickEvent> predicate;
    private final BiConsumer<MenuHolder<?>, InventoryClickEvent> predicateFailedCallback;
    protected MenuAction delegate;

    public PredicateAction(MenuAction delegate, BiPredicate<MenuHolder<?>, InventoryClickEvent> predicate) {
        this(delegate, predicate, null);
    }

    public PredicateAction(MenuAction delegate, BiPredicate<MenuHolder<?>, InventoryClickEvent> predicate,
                           BiConsumer<MenuHolder<?>, InventoryClickEvent> predicateFailedCallback) {
        this.delegate = Objects.requireNonNull(delegate, "Delegate button cannot be null");
        this.predicate = Objects.requireNonNull(predicate, "Predicate cannot be null");
        this.predicateFailedCallback = predicateFailedCallback;
    }

    @Override
    public final void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
        if (getPredicate().test(menu, event)) {
            getDelegate().onClick(menu, event);
        } else {
            getPredicateFailedCallback().ifPresent(callback -> callback.accept(menu, event));
        }
    }

    protected MenuAction getDelegate() {
        return delegate;
    }

    protected BiPredicate<MenuHolder<?>, InventoryClickEvent> getPredicate() {
        return predicate;
    }

    protected Optional<BiConsumer<MenuHolder<?>, InventoryClickEvent>> getPredicateFailedCallback() {
        return Optional.ofNullable(predicateFailedCallback);
    }
}
