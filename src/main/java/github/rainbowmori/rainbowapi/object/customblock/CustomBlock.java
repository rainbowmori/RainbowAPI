package github.rainbowmori.rainbowapi.object.customblock;

import com.google.gson.JsonObject;
import github.rainbowmori.rainbowapi.RMPlugin;
import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.object.cache.CacheData;
import github.rainbowmori.rainbowapi.object.cache.CacheManager;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 左クリック、右クリックで処理を受け取るカスタムブロック {@link #clearData(Location)} を自分で拡張して {@link #remove()} でブロックを削除できます
 * <p>
 * サーバー起動時に登録する場合は {@link RMPlugin#registerBlock(String, Class)} で登録してください サーバーが起動中に登録する場合は
 * {@link #register(String, Class)} で登録してください
 */
public abstract class CustomBlock {

  //ここでCustomBlockに関するcacheを保存したい場合は使用してください
  public static final CacheManager<UUID, CacheData<UUID>> blockCache = new CacheManager<>();

  private static final TreeMap<String, Class<? extends CustomBlock>> customBlocks = new TreeMap<>();
  private static final Map<Location, CustomBlock> blocks = new HashMap<>();

  private final Location location;

  /**
   * カスタムブロックの初期化コンストラクタ
   *
   * @param location カスタムブロックの位置
   */
  public CustomBlock(Location location) {
    this.location = location.toBlockLocation();
    if (blocks.containsKey(this.location)) {
      throw new RuntimeException("このBlockはすでにCustomBlockです");
    }
    blocks.put(this.location, this);
  }

  /**
   * ここで鯖起動もしくは再起動時に独自の値を持たせている場合に{@link #getBlockData()} 保存したものを読み込むためのコンストラクタです
   *
   * @param location カスタムブロックの位置
   * @param data     ロードするデータ
   */
  public CustomBlock(Location location, JsonObject data) {
    this(location);
  }

  /**
   * これはブロックをプレイヤーが設置したという風に処理したい場合に使用します
   *
   * @param location カスタムブロックの位置
   * @param player   設置したプレイヤー
   */
  public CustomBlock(Location location, Player player) {
    this(location);
  }

  static Map<Location, CustomBlock> getBlocks() {
    return blocks;
  }

  /**
   * この位置のブロックを削除します
   *
   * @param location 削除する位置
   */
  public static void remove(Location location) {
    Location block = location.toBlockLocation();
    if (isCustomBlock(block)) {
      getCustomBlock(block).clearData(location);
      blocks.remove(block);
    }
  }

  /**
   * {@link #remove(Location)} では {@link #clearData(Location)} を起こしてしまうのでそれをしないメゾットです
   * @param location 削除する位置
   */
  public static void silentRemove(Location location) {
    blocks.remove(location.toBlockLocation());
  }

  /**
   * {@link #remove(Location)} では {@link #clearData(Location)} を起こしてしまうのでそれをしないメゾットです
   * @param block block
   */
  public static void silentRemove(Block block) {
    silentRemove(block.getLocation());
  }

  /**
   * {@link #remove(Location)} をブロックでできるように
   *
   * @param block block
   */
  public static void remove(Block block) {
    remove(block.getLocation());
  }

  /**
   * ここでブロックの登録をおこないます
   *
   * @param identifier ブロックの識別子
   * @param clazz      作成したカスタムブロックのclass
   */
  public static void register(String identifier, Class<? extends CustomBlock> clazz) {
    customBlocks.put(identifier, clazz);
    RainbowAPI.getPlugin().getPrefixUtil().logDebug(
        "<green>REGISTER CUSTOM BLOCK [identifier=\"%s\",class=\"%s\"]".formatted(identifier,
            clazz.getName()));
  }

  /**
   * この識別子でカスタムブロックが登録されているかどうかを確認します
   *
   * @param identifier カスタムブロックの識別子
   * @return trueの場合すでに登録されています
   */
  public static boolean isRegister(String identifier) {
    return customBlocks.containsKey(identifier);
  }

  /**
   * このLocationにカスタムブロックが存在するかどうか
   *
   * @param location カスタムブロックの場所
   * @return trueの場合孫座します
   */
  public static boolean isCustomBlock(Location location) {
    return blocks.containsKey(location.toBlockLocation());
  }

  /**
   * このBlockにカスタムブロックが存在するかどうか
   *
   * @param block block
   * @return trueの場合孫座します
   */
  public static boolean isCustomBlock(Block block) {
    return isCustomBlock(block.getLocation());
  }

  /**
   * blockにあるカスタムブロックの識別子を取得します
   *
   * @param block 取得するカスタムブロックのブロック
   * @return カスタムブロックの識別子
   */
  @NotNull
  public static String getIdentifier(Block block) {
    return getIdentifier(block.getLocation());
  }

  /**
   * locationにあるカスタムブロックの識別子を取得します
   *
   * @param location 取得するカスタムブロックの場所
   * @return カスタムブロックの識別子
   */
  @NotNull
  public static String getIdentifier(Location location) {
    Location bLoc = location.toBlockLocation();
    if (isCustomBlock(bLoc)) {
      return getCustomBlock(bLoc).getIdentifier();
    }
    throw new IllegalArgumentException("そのBlockはCustomBlockではありません(もしくは登録されていません)");
  }

  /**
   * {@link #getCustomBlock(Location)} を {@link Block} でも取得できるように
   *
   * @param block 対象の場所
   * @return 取得したCustomBlock
   * @throws IllegalArgumentException そのlocationにCustomBlockがない場合はなります
   */
  @NotNull
  public static CustomBlock getCustomBlock(Block block) {
    return getCustomBlock(block.getLocation());
  }

  /**
   * locationから存在する場合はCustomBlockを返します
   *
   * @param location 取得する場所
   * @return 取得したCustomBlock
   * @throws IllegalArgumentException そのlocationにCustomBlockがない場合はなります
   */
  @NotNull
  public static CustomBlock getCustomBlock(Location location) {
    var block = location.toBlockLocation();
    if (isCustomBlock(block)) {
      return blocks.get(block);
    }
    throw new IllegalArgumentException("そのBlockはCustomBlockではありません");
  }

  static void loadCustomBlock(String identifier, Location location, JsonObject object) {
    if (!isRegister(identifier)) {
      throw new IllegalArgumentException(identifier + "のCustomBlockは登録されていません");
    }
    try {
      CustomBlock.customBlocks.get(identifier)
          .getDeclaredConstructor(Location.class, JsonObject.class).newInstance(location, object);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@link #remove(Location)} をinstance methodから呼び出せるように
   */
  public final void remove() {
    remove(getLocation());
  }

  /**
   * {@link #silentRemove(Location)} を instance method から呼び出せるように
   */
  public final void silentRemove() {
    silentRemove(getLocation());
  }

  /**
   * {@link CustomModelBlock#clearData(Location)} のようにブロックを削除する場合の処理
   *
   * @param Location このブロックのlocation
   */
  public abstract void clearData(Location Location);

  /**
   * 何かブロックにデータを保存させたい場合はここにデータを保存してください
   *
   * @return data
   */
  public JsonObject getBlockData() {
    return new JsonObject();
  }

  /**
   * 左クリックの処理
   *
   * @param e event
   */
  public void leftClick(PlayerInteractEvent e) {

  }

  /**
   * 右クリックの処理
   *
   * @param e event
   */
  public void rightClick(PlayerInteractEvent e) {

  }

  /**
   * プレイヤーがクリエイティブかどうか
   *
   * @param player 対象
   * @return trueでクリエイティブ
   * @apiNote 多分これは消すかもしれん
   */
  protected boolean isCreative(Player player) {
    return player.getGameMode().equals(GameMode.CREATIVE);
  }

  /**
   * このCustomBlockのBlockLocationを返します
   *
   * @return location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * ブロックの識別子を取得します
   *
   * @return 識別子
   */
  public abstract String getIdentifier();

}
