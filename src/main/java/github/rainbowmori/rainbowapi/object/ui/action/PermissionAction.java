package github.rainbowmori.rainbowapi.object.ui.action;

import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PermissionAction extends PredicateAction {

    private final Consumer<? super HumanEntity> noPermissionCallback;
    private String permission;

    public PermissionAction(String permission, MenuAction action) {
        this(permission, action, null);
    }

    public PermissionAction(String permission, MenuAction action, Consumer<? super HumanEntity> noPermissionCallback) {
        super(action, (menuHolder, event) -> event.getWhoClicked().hasPermission(permission));
        this.permission = Objects.requireNonNull(permission, "Permission cannot be null");
        this.noPermissionCallback = noPermissionCallback;
    }

    public final String getPermission() {
        return permission;
    }

    public final void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    protected Optional<BiConsumer<MenuHolder<?>, InventoryClickEvent>> getPredicateFailedCallback() {
        return getNoPermissionCallback().map(consumer -> (menuHolder, event) -> consumer.accept(event.getWhoClicked()));
    }

    public final Optional<Consumer<? super HumanEntity>> getNoPermissionCallback() {
        return Optional.ofNullable(noPermissionCallback);
    }

    @Override
    public String toString() {
        return "permission " + permission + " " + delegate.toString();
    }
}
