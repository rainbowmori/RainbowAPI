package github.rainbowmori.rainbowapi.object.ui.action;

import com.google.gson.JsonParser;
import github.rainbowmori.rainbowapi.object.ui.gui.MenuHolder;
import github.rainbowmori.rainbowapi.util.JsonUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface MenuAction {
    static MenuAction createAction(String consumer) {
        String[] args = consumer.split(" ");
        return switch (args[0]) {
            case "chat" -> new ChatAction(collect(args));
            case "close" -> new CloseAction();
            case "console" -> new ConsoleCommandAction(collect(args));
            case "op" -> new OPCommandAction(collect(args));
            case "permission" -> new PermissionAction(args[1], createAction(
                    Arrays.stream(args, 2, args.length).collect(Collectors.joining(" "))));
            case "command" -> new PlayerCommandAction(collect(args));
            case "teleport" -> new TeleportAction(JsonUtil.decodeLocation(JsonParser.parseString(
                    collect(args))));
            default -> throw new RuntimeException("buttonのargがおかしいです" + consumer);
        };
    }

    private static String collect(String[] args) {
        return Arrays.stream(args, 1, args.length).collect(Collectors.joining());
    }

    default void onClick(MenuHolder<?> menu, InventoryClickEvent event) {
    }
}
