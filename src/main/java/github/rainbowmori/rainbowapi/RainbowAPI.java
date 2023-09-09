package github.rainbowmori.rainbowapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import github.rainbowmori.rainbowapi.api.serializer.ItemStackSerializer;
import github.rainbowmori.rainbowapi.api.serializer.LocationSerializer;
import github.rainbowmori.rainbowapi.commnad.RainbowAPICommand;
import github.rainbowmori.rainbowapi.listener.BlockBreak;
import github.rainbowmori.rainbowapi.listener.CustomListeners;
import github.rainbowmori.rainbowapi.object.DataManager;
import github.rainbowmori.rainbowapi.object.customblock.CustomBlock;
import github.rainbowmori.rainbowapi.object.cutomitem.CustomItem;
import github.rainbowmori.rainbowapi.dependencies.ui.GuiListener;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * プラグインのメインクラス
 */
public final class RainbowAPI extends RMPlugin {

  /**
   * {@link Location} and {@link ItemStack} を Serializer and Deserializer できる {@link Gson}
   */
  public static final Gson gson = new GsonBuilder().
      registerTypeAdapter(ItemStack.class, new ItemStackSerializer()).
      registerTypeAdapter(Location.class, new LocationSerializer()).create();

  private static DataManager dataManager;
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
    registerEvent(CustomListeners.getInstance());
    registerEvent(guiListener = GuiListener.getInstance());

    CommandAPI.onEnable();

    registerCommand(new RainbowAPICommand());

    new BukkitRunnable() {
      @Override
      public void run() {
        customItems.forEach(CustomItem::register);
        customBlocks.forEach(CustomBlock::register);
        dataManager = new DataManager(RainbowAPI.this);
      }
    }.runTaskLater(this, 1L);
  }

  @Override
  public void onDisable() {
    registeredCommands.forEach(CommandAPI::unregister);
    CommandAPI.onDisable();
    dataManager.save();
  }

  @Override
  public String getPrefix() {
    return "<gray>[<red>RM<gray>]";
  }

  @Override
  public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this).initializeNBTAPI(NBTContainer.class, NBTContainer::new));
  }

}
