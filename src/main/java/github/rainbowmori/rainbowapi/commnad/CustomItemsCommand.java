package github.rainbowmori.rainbowapi.commnad;

import dev.jorel.commandapi.CommandAPICommand;
import github.rainbowmori.rainbowapi.RMPlugin;
import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.dependencies.anvilgui.GUIs;
import github.rainbowmori.rainbowapi.object.cutomitem.CustomItem;
import github.rainbowmori.rainbowapi.dependencies.ui.button.ItemButton;
import github.rainbowmori.rainbowapi.dependencies.ui.menu.MenuHolder;
import github.rainbowmori.rainbowapi.dependencies.ui.menu.PageMenu;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.wesjd.anvilgui.AnvilGUI.ResponseAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemsCommand extends CommandAPICommand {

  public CustomItemsCommand() {
    super("customitems");
    executesPlayer((sender, args) -> {
      open(sender);
    });
  }

  private static void open(Player sender) {
    var map = CustomItem.customItems.entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, e -> CustomItem.getDefaultCustomItem(e.getKey()),(v1, v2) -> v1, TreeMap::new));
    sender.openInventory(new ClaimItemsMenu(RainbowAPI.getPlugin(), 45, map).getInventory());
  }

  private static final class ClaimItemsMenu extends PageMenu<RMPlugin> {

    public ClaimItemsMenu(RMPlugin plugin, int pageSize, TreeMap<String, CustomItem> rewards) {
      this(plugin, pageSize, rewards, 0, Math.min(rewards.size(), pageSize));
    }

    private ClaimItemsMenu(RMPlugin plugin, int pageSize, TreeMap<String, CustomItem> rewards,
        int rewardStartIndex, int rewardEndIndex) {
      super(plugin, new ItemPage(plugin, pageSize, rewards, rewardStartIndex, rewardEndIndex),
          "<green>CustomItems", null, null);
    }

    @Override
    public void onOpen(InventoryOpenEvent openEvent) {
      super.onOpen(openEvent);
      setButton(getPageSize(), new ItemButton<>(
          new ItemBuilder(Material.ENDER_PEARL).name("<green>最初のページにもどる").build()) {
        @Override
        public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
          open(((Player) event.getWhoClicked()));
        }
      });
      setButton(getPageSize() + 8,
          new ItemButton<>(new ItemBuilder(Material.BOOK).name("<aqua>クリックで文字検索").build()) {
            @Override
            public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
              GUIs.of(getPlugin()).getStringInput("&b文字で検索", (s, completion) -> List.of(
                      ResponseAction.openInventory(
                          new ClaimItemsMenu(getPlugin(), 45, CustomItem.customItems.entrySet().stream()
                              .filter(entry -> entry.getKey().toLowerCase().contains(s.toLowerCase()))
                              .collect(Collectors.toMap(Map.Entry::getKey,
                                      e -> CustomItem.getDefaultCustomItem(e.getKey()),
                                      (v1, v2) -> v1, TreeMap::new))).getInventory())))
                  .setCloseable(player -> player.openInventory(getInventory()))
                  .open(((Player) event.getWhoClicked()));
            }
          });
    }

    @Override
    public ItemPage getPage() {
      return (ItemPage) super.getPage();
    }

    @Override
    protected boolean needsRedirects() {
      return false;
    }

    private static final class ItemPage extends MenuHolder<RMPlugin> {

      private final int rewardStartIndex, rewardEndIndex;
      private final TreeMap<String, CustomItem> rewards;

      private ItemPage(RMPlugin plugin, int pageSize, TreeMap<String, CustomItem> rewards,
          int rewardStartIndex, int rewardEndIndex) {
        super(plugin, pageSize);
        this.rewardStartIndex = rewardStartIndex;
        this.rewardEndIndex = rewardEndIndex;
        this.rewards = rewards;
      }

      @Override
      public void onOpen(InventoryOpenEvent event) {
        for (int slot = 0;
            slot < getInventory().getSize() && rewardStartIndex + slot < rewardEndIndex; slot++) {
          List<String> list = rewards.keySet().stream().toList();
          String key = list.get(rewardStartIndex + slot);
          ItemStack item = rewards.get(key).getItem();
          setButton(slot,
              new ItemButton<>(new ItemBuilder(item).addLore("").addLore(key).build()) {
                @Override
                public void onClick(MenuHolder<?> holder, InventoryClickEvent event) {
                  event.getWhoClicked().getInventory().addItem(item.clone());
                }
              });
        }
      }

      @Override
      public void onClose(InventoryCloseEvent event) {
        clearButtons();
      }
    }
  }
}