package github.rainbowmori.rainbowapi;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import github.rainbowmori.rainbowapi.api.serializer.ItemStackSerializer;
import github.rainbowmori.rainbowapi.api.serializer.LocationSerializer;
import github.rainbowmori.rainbowapi.dependencies.ui.GuiListener;
import github.rainbowmori.rainbowapi.listener.BlockBreak;

/**
 * プラグインのメインクラス
 */
public final class RainbowAPI extends RMPlugin {

  /**
   * {@link Location} and {@link ItemStack} を Serializer and Deserializer
   * できる {@link Gson}
   */
  public static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
      .registerTypeAdapter(Location.class, new LocationSerializer())
      .create();

  private static GuiListener guiListener;

  public static GuiListener getGuiListener() {
    return guiListener;
  }

  public static RainbowAPI getPlugin() {
    return getPlugin(RainbowAPI.class);
  }

  @Override
  public void onEnable() {
    registerEvent(BlockBreak.getInstance());
    registerEvent(guiListener = GuiListener.getInstance());

    CommandAPI.onEnable();

  }

  @Override
  public void onDisable() {
    registeredCommands.forEach(CommandAPI::unregister);
    CommandAPI.onDisable();
  }

  @Override
  public String getPrefix() {
    return "<gray>[<red>RM<gray>]";
  }

  @Override
  public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this).initializeNBTAPI(
        NBTContainer.class, NBTContainer::new));
  }
}
