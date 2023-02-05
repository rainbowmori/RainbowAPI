package github.rainbowmori.rainbowapi.object;

import org.bukkit.event.inventory.ClickType;

public enum ItemClickType {
    ALL,
    RIGHT,
    SHIFT_RIGHT,
    LEFT,
    SHIFT_LEFT,
    DOUBLE_CLICK;

    public static boolean isItemClickType(ClickType type) {
        return type.isRightClick() || type.isLeftClick() && !type.isCreativeAction();
    }
}
