package github.rainbowmori.rainbowapi.dependencies.anvilgui;

import github.rainbowmori.rainbowapi.util.ItemBuilder;
import github.rainbowmori.rainbowapi.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;
import net.wesjd.anvilgui.AnvilGUI.ResponseAction;
import net.wesjd.anvilgui.AnvilGUI.StateSnapshot;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class GUIs {

  public final Plugin plugin;
  private Builder builder;

  private GUIs(Plugin plugin) {
    this.plugin = plugin;
  }

  public static GUIs of(Plugin plugin) {
    return new GUIs(plugin);
  }

  public AnvilGUI open(Player player) {
    return builder.open(player);
  }

  public GUIs consumer(Consumer<Builder> consumer) {
    consumer.accept(builder);
    return this;
  }

  public GUIs getIntegerInput(String title,
      Predicate<Integer> predicate,
      BiFunction<Integer, StateSnapshot, List<ResponseAction>> yes,
      BiFunction<Integer, StateSnapshot, List<ResponseAction>> no) {
    return getInput(title,
        s -> NumberUtils.isDigits(s) ? Optional.of(Integer.parseInt(s)) : Optional.empty(),
        s -> List.of(ResponseAction.replaceInputText("数字を入力してください")), predicate, yes, no);
  }

  public GUIs getStringInput(String title,
      Predicate<String> predicate,
      BiFunction<String, StateSnapshot, List<ResponseAction>> yes,
      BiFunction<String, StateSnapshot, List<ResponseAction>> no) {
    return getInput(title, Optional::of, null, predicate, yes, no);
  }

  public GUIs getStringInput(String title,
      BiFunction<String, StateSnapshot, List<ResponseAction>> function) {
    return getInput(title, Optional::of, null, s -> true, function, null);
  }

  public GUIs setCloseable(Consumer<Player> consumer) {
    builder.onClose(state -> plugin.getServer().getScheduler().runTask(plugin, () -> {
      Player player = state.getPlayer();
      InventoryType type = player.getOpenInventory().getType();
      if (type == InventoryType.CRAFTING || type == InventoryType.CREATIVE) {
        consumer.accept(player);
      }
    }));
    return this;
  }

  public <T> GUIs getInput(String title,
      Function<String, Optional<T>> toClazz, Function<String, List<ResponseAction>> cantConvert,
      Predicate<T> predicate,
      BiFunction<T, StateSnapshot, List<ResponseAction>> yes,
      BiFunction<T, StateSnapshot, List<ResponseAction>> no) {
    this.builder = new Builder().plugin(plugin).title(Util.cc(Util.serializeLegacy(Util.mm(title))))
        .itemLeft(new ItemBuilder(Material.PAPER).name("").build())
        .onClick((slot,state) -> {
          if (slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
          }
          String input = state.getText();
          Optional<T> apply = toClazz.apply(input);
          if (apply.isEmpty()) {
            return cantConvert.apply(input);
          }
          T t = apply.get();
          if (predicate.test(t)) {
            return yes.apply(t, state);
          } else {
            return no.apply(t, state);
          }
        });
    return this;
  }

}
