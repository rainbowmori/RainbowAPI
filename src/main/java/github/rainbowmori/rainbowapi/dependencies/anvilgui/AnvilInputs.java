package github.rainbowmori.rainbowapi.dependencies.anvilgui;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import github.rainbowmori.rainbowapi.util.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.StateSnapshot;

public class AnvilInputs {
  private AnvilInputs() {
  }

  public static <T> AnvilGUI.Builder getInput(@NotNull Plugin plugin, @NotNull String title,
      @NotNull BiFunction<String, StateSnapshot, T> toClazz,
      @NotNull BiFunction<T, StateSnapshot, List<AnvilGUI.ResponseAction>> func) {
    return getInput(plugin, title, (s, state) -> Optional.of(toClazz.apply(s, state)),
        (s, state) -> Collections.emptyList(), func);
  }

  public static <T> AnvilGUI.Builder getInput(@NotNull Plugin plugin, @NotNull String title,
      @NotNull BiFunction<String, StateSnapshot, Optional<T>> toClazz,
      @NotNull BiFunction<String, StateSnapshot, List<AnvilGUI.ResponseAction>> non,
      @NotNull BiFunction<T, StateSnapshot, List<AnvilGUI.ResponseAction>> func) {
    return new AnvilGUI.Builder().plugin(plugin).title(title)
        .itemLeft(new ItemBuilder(Material.PAPER).name("").build())
        .onClick((slot, state) -> {
          if (slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
          }

          String input = state.getText();
          Optional<T> apply = toClazz.apply(input, state);
          if (apply.isEmpty()) {
            return non.apply(input, state);
          }
          return func.apply(apply.get(), state);
        });
  }
}
