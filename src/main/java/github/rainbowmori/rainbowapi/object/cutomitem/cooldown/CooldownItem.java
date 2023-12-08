package github.rainbowmori.rainbowapi.object.cutomitem.cooldown;

import github.rainbowmori.rainbowapi.object.cutomitem.CustomItem;
import github.rainbowmori.rainbowapi.object.cutomitem.InterfaceItem;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link CustomItem} にクールダウンの概念を持たす機能クラスです
 */
public interface CooldownItem extends InterfaceItem {

  /**
   * プレイヤーが識別子のクールダウンを持っているか確認します
   * 
   * @param uuid       player's uuid
   * @param identifier 識別子
   * @return クールダウン中の場合trueを返します
   */
  static boolean hasCooldown(UUID uuid, String identifier) {
    return getPlayerCooldown(uuid).hasCooldown(identifier);
  }

  /**
   * プレイヤーの識別子のクールダウンを整数で取得します
   * 
   * @param uuid       player's uuid
   * @param identifier 識別子
   * @return 残りのクールダウンを返します。(ない場合は0を返します)
   */
  static int getIntCooldown(UUID uuid, String identifier) {
    return getPlayerCooldown(uuid).getIntCooldown(identifier);
  }

  /**
   * プレイヤーの識別子のクールダウンを少数まで取得します
   * 
   * @param uuid       player's uuid
   * @param identifier 識別子
   * @return 残りのクールダウンを返します。(ない場合は0を返します)
   */
  static double getDoubleCooldown(UUID uuid, String identifier) {
    return getPlayerCooldown(uuid).getDoubleCooldown(identifier);
  }

  /**
   * プレイヤー識別子のクールダウンの秒数に追加します
   * クールダウンがない場合はクールダウンがその時間になります
   * 
   * @param uuid       player's uuid
   * @param identifier 識別子
   * @param cooldown   追加する時間
   * @return 追加するときにクールダウンが設定されていなかった場合trueが返されます
   *         (要するに{@link #hasCooldown(UUID, String)} ってことです)
   */
  static boolean addCooldown(UUID uuid, String identifier, double cooldown) {
    return getPlayerCooldown(uuid).addCooldown(new ItemCooldown(identifier, cooldown));
  }

  /**
   * プレイヤーの識別子のクールダウンを設定します
   * 
   * @param uuid       player's uuid
   * @param identifier 識別子
   * @param cooldown   cooldown
   */
  static void setCooldown(UUID uuid, String identifier, double cooldown) {
    getPlayerCooldown(uuid).setCooldown(new ItemCooldown(identifier, cooldown));
  }

  /**
   * プレイヤーのクールダウン情報を取得します
   * 
   * @param uuid player's uuid
   * @return 情報
   */
  @NotNull
  static PlayerItemCooldown getPlayerCooldown(UUID uuid) {
    return CustomItem.itemCache.computeGetCache(uuid, new PlayerItemCooldown(uuid));
  }

  @Override
  default @NotNull Optional<String> getActionBarMessage(Player player) {
    return Optional.of(getCooldownMessage(player.getUniqueId()));
  }

  /**
   * <p>
   * クールダウンのありなしの場合で文字を
   * </p>
   * {@link #getHasCooldownMessage(UUID)},{@link #getReadyMessage(UUID)} に変えます
   * <p>
   * 基本的に変更する場合はそちらを変えてください
   * </p>
   * 
   * @param uuid player's uuid
   * @return メッセージ
   */
  @NotNull
  default String getCooldownMessage(UUID uuid) {
    return hasCooldown(uuid) ? getHasCooldownMessage(uuid) : getReadyMessage(uuid);
  }

  /**
   * クールダウン中の場合に {@link #getActionBarMessage(Player)} に表示するメッセージ
   * 
   * @param uuid player's uuid
   * @return クールダウン中のメッセージ
   */
  @NotNull
  default String getHasCooldownMessage(UUID uuid) {
    return "<red>Now on " + getCooldown(uuid) + " cooldown";
  }

  /**
   * {@link #getHasCooldownMessage(UUID)} このときに表示するくーるダウンの秒数の処理
   * 
   * @param uuid player's uuid
   * @return クールダウンの秒数
   */
  @NotNull
  default Number getCooldown(UUID uuid) {
    return getIntCooldown(uuid);
  }

  /**
   * クールダウンがない場合に {@link #getActionBarMessage(Player)} に表示するメッセージ
   * 
   * @param uuid player's uuid
   * @return クールダウンがないときのメッセージ
   */
  @NotNull
  default String getReadyMessage(UUID uuid) {
    return "<green>READY";
  }

  /**
   * クールダウン中かを確認します
   * 
   * @param uuid player's uuid
   * @return クールダウン中の場合trueを返します
   */
  default boolean hasCooldown(UUID uuid) {
    return hasCooldown(uuid, getIdentifier());
  }

  /**
   * クールダウンを整数で取得します
   * 
   * @param uuid player's uuid
   * @return 整数のクールダウン
   */
  default int getIntCooldown(UUID uuid) {
    return getIntCooldown(uuid, getIdentifier());
  }

  /**
   * クールダウンをdouble小数点まで取得します
   * 
   * @param uuid player's uuid
   * @return 小数点のあるクールダウン
   */
  default double getDoubleCooldown(UUID uuid) {
    return getDoubleCooldown(uuid, getIdentifier());
  }

  /**
   * クールダウンの秒数に追加します
   * クールダウンがない場合はクールダウンがその時間になります
   * 
   * @param uuid     player's uuid
   * @param cooldown 追加する時間
   * @return 追加するときにクールダウンが設定されていなかった場合trueが返されます
   *         (要するに{@link #hasCooldown(UUID, String)} ってことです)
   */
  default boolean addCooldown(UUID uuid, double cooldown) {
    return addCooldown(uuid, getIdentifier(), cooldown);
  }

  /**
   * プレイヤーのクールダウンを設定します
   * 
   * @param uuid     player's uuid
   * @param cooldown cooldown
   */
  default void setCooldown(UUID uuid, double cooldown) {
    setCooldown(uuid, getIdentifier(), cooldown);
  }
}
