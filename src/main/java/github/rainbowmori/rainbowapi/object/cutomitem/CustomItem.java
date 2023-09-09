package github.rainbowmori.rainbowapi.object.cutomitem;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import github.rainbowmori.rainbowapi.RMPlugin;
import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.object.cache.CacheData;
import github.rainbowmori.rainbowapi.object.cache.CacheManager;
import github.rainbowmori.rainbowapi.util.FormatterUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * 左、右クリックやアイテムをもってシフトしたりアイテムをこれに変えたりほかのに変えたeventを受け取り処理するアイテムのabstract classです
 * サーバー起動時に登録する場合は {@link RMPlugin#registerItem(String, Class)} で登録してください
 * サーバーが起動中に登録する場合は {@link #register(String, Class)} で登録してください
 * <p>
 * 引数を持たないコンストラクタを作成してそこで this(ItemStack) でそのアイテムがデフォルトとして登録されます
 */
public abstract class CustomItem implements InterfaceItem {

  //ここでCooldownのcacheなどを保存していますのでCustomItem関連のcacheを保存したい場合は使用してください
  public static final CacheManager<UUID, CacheData<UUID>> itemCache = new CacheManager<>();

  //ここに登録されたcustomItemの識別子をキーとしてその識別子を持つクラスをMapとして保存しています
  public static final TreeMap<String, Class<? extends CustomItem>> customItems = new TreeMap<>();

  private final ItemStack item;

  /**
   * ここはeventを受け取りそのアイテムでインスタンスを作成するコンストラクタです
   * ここで {@link NBT} の static methodを使用したりしてデータを読み込んで臭い
   * @param item カスタムアイテムのitemstack
   */
  public CustomItem(ItemStack item) {
    if (!isRegister(getIdentifier())) {
      throw new RuntimeException(FormatterUtil.format(
          "{name}はCustomItemに登録されていません! CustomItem.register({identifier},{name})",
          Map.of("name", getClass().getSimpleName(), "identifier", getIdentifier())));
    }
    NBT.modify(item, nbt -> {
      nbt.getOrCreateCompound(nbtKey).setString("identifier", getIdentifier());
    });
    this.item = item;
  }

  /**
   * カスタムアイテムの登録methodです、ですがサーバー起動時に登録する場合は {@link RMPlugin#registerItem(String, Class)} を使用してください
   * @param identifier 識別子
   * @param clazz その識別子をもつCustomItem Class
   */
  public static void register(String identifier, Class<? extends CustomItem> clazz) {
    if (isRegister(identifier)) {
      throw new IllegalArgumentException(identifier + "はすでにCustomItemに登録されています");
    }
    customItems.put(identifier, clazz);
    RainbowAPI.getPlugin().getPrefixUtil().logDebug(
        "<green>REGISTER CUSTOM ITEM [identifier=\"%s\",class=\"%s\"]".formatted(identifier,
            clazz.getName()));
  }

  /**
   * 登録解除するためのメゾットです、ですがサーバーストップ時に登録されているデータはすべて消えるので、
   * 基本的には使用することがないメゾットです。
   * @param identifier 識別子
   */
  public static void unregister(String identifier) {
    if (!isRegister(identifier)) {
      throw new IllegalArgumentException(identifier + "はCustomItemに登録されていません");
    }
    RainbowAPI.getPlugin().getPrefixUtil().logDebug(
        "<red>UNREGISTER CUSTOM ITEM [identifier=\"%s\",class=\"%s\"]".formatted(identifier,
            customItems.get(identifier).getName()));
    customItems.remove(identifier);
  }

  /**
   * その識別子が登録されているか
   * @param identifier 識別子
   * @return 登録されてるとtrueが返されます
   */
  public static boolean isRegister(String identifier) {
    return customItems.containsKey(identifier);
  }

  /**
   * アイテムがその識別子なのかを判別します
   * @param identifier 識別子
   * @param item 判定するアイテム
   * @return 同じ識別子の場合trueを返します
   */
  public static boolean equalsIdentifier(String identifier, ItemStack item) {
    if (!isRegister(identifier)) {
      return false;
    }
    if (!isCustomItem(item)) {
      return false;
    }
    return getIdentifier(item).equals(identifier);
  }

  /**
   * どちらのアイテムも同じ識別子かどうか判別します
   * @param itemStack item1
   * @param item item2
   * @return 同じだった場合trueを返します
   */
  public static boolean equalsIdentifier(ItemStack itemStack, ItemStack item) {
    if (!isCustomItem(itemStack)) {
      return false;
    }
    return equalsIdentifier(getIdentifier(itemStack), item);
  }

  /**
   * そのアイテムがもつ識別子が登録されているかを判別します
   * @param item 対象のアイテム
   * @return 登録されいたらtrueを返します
   */
  public static boolean isCustomItem(ItemStack item) {
    if (item == null || item.getType().isAir()) {
      return false;
    }
    return NBT.get(item, readableNBT -> {
      if (!readableNBT.hasTag(nbtKey)) {
        return false;
      }
      ReadableNBT compound = readableNBT.getCompound(nbtKey);
      return compound.hasTag("identifier") && isRegister(compound.getString("identifier"));
    });
  }

  /**
   * そのアイテムから識別子を取得します
   * @param item 対象のアイテム
   * @return アイテムの識別子を返します
   * @throws IllegalArgumentException 渡されたアイテムがCustomItemではない場合にthrowされます
   */
  @NotNull
  public static String getIdentifier(ItemStack item) {
    if (isCustomItem(item)) {
      return NBT.get(item, readableNBT -> readableNBT.getCompound(nbtKey).getString("identifier"));
    }
    throw new IllegalArgumentException("そのアイテムはCustomItemではありません(もしくは登録されていません)");
  }

  /**
   * その識別子のアイテムの引数のないコンストラクタから作成しインスタンスを返します
   * @param identifier 作成したアイテムの識別子
   * @return 作成したカスタムアイテムのインスタンス
   */
  @NotNull
  public static CustomItem getDefaultCustomItem(String identifier) {
    if (isRegister(identifier)) {
      try {
        return customItems.get(identifier).getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
               NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
    throw new IllegalArgumentException("そのアイテムはCustomItemではありません");
  }

  /**
   * そのアイテムからカスタムアイテムのインスタンスを作成します
   * @param item 対象のアイテム
   * @return 作成したカスタムアイテムのインスタンス
   */
  @NotNull
  public static CustomItem getCustomItem(ItemStack item) {
    if (isCustomItem(item)) {
      try {
        return customItems.get(getIdentifier(item)).getDeclaredConstructor(ItemStack.class)
            .newInstance(item);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
               NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
    throw new IllegalArgumentException("そのアイテムはCustomItemではありません");
  }

  /**
   * 左クリック時の処理
   * デフォルトではeventはキャンセルされますが {@code e.setCancelled(false)}
   * をすることでキャンセルするのを防げます
   * @param e event
   */
  public void leftClick(PlayerInteractEvent e) {

  }

  /**
   * 右クリック時の処理
   * デフォルトではeventはキャンセルされますが {@code e.setCancelled(false)}
   *  をすることでキャンセルするのを防げます
   * @param e event
   */
  public void rightClick(PlayerInteractEvent e) {

  }

  /**
   * ほかのアイテムからこのカスタムアイテムにswapした時の処理
   * @param e event
   */
  public void heldOfThis(PlayerItemHeldEvent e) {
    Player player = e.getPlayer();
    new BukkitRunnable() {
      private final int previousSlot = e.getNewSlot();

      @Override
      public void run() {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (previousSlot != player.getInventory().getHeldItemSlot() || !equalsIdentifier(
            getIdentifier(), item)) {
          this.cancel();
          return;
        }
        heldItem().ifPresent(consumer -> consumer.accept(player));
      }
    }.runTaskTimer(RainbowAPI.getPlugin(), 0L, 1L);
  }

  /**
   * このアイテムからほかのにswapした時の処理
   * @param e event
   */
  public void heldOfOther(PlayerItemHeldEvent e) {

  }

  /**
   * このアイテムをもちsneakをするときの処理
   * @param e event
   */
  public void shiftItem(PlayerToggleSneakEvent e) {

  }

  /**
   * クリックしたブロックの一つ前のブロックで処理をします
   * @param e event
   * @param consumer 処理
   */
  protected final void relativeBlock(PlayerInteractEvent e, Consumer<Block> consumer) {
    if (!clickedBlock(e)) {
      throw new RuntimeException("clickedBlockがnullなのにrelativeBlockメゾットを呼びました");
    }
    consumer.accept(e.getClickedBlock().getRelative(e.getBlockFace()));
  }

  /**
   * クリックしたブロックの一つ前のlocationで処理をします
   * @param e event
   * @param consumer 処理
   */
  protected final void relativeLocation(PlayerInteractEvent e, Consumer<Location> consumer) {
    relativeBlock(e, block -> consumer.accept(block.getLocation()));
  }

  /**
   * アイテムのamountを-1します
   */
  protected final void itemUse() {
    getItem().setAmount(getItem().getAmount()-1);
  }

  /**
   * ブロックをクリックしたか、ブロックがairではないかどうかを教えます
   * @param e event
   * @return ブロックがある場合trueを返します
   */
  protected final boolean clickedBlock(PlayerInteractEvent e) {
    Block clickedBlock = e.getClickedBlock();
    return clickedBlock != null && !clickedBlock.getType().isAir();
  }

  /**
   * このカスタムアイテムのインスタンスで使用しているアイテムを返します
   * @return itemstack
   */
  @Override
  public @NotNull ItemStack getItem() {
    return item;
  }
}

