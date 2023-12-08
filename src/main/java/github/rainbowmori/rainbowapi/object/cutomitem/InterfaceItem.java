package github.rainbowmori.rainbowapi.object.cutomitem;

import github.rainbowmori.rainbowapi.util.Util;
import java.util.Optional;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * {@link CustomItem} の拡張機能を作成するために使用できます
 * <p>
 * {@link github.rainbowmori.rainbowapi.object.cutomitem.cooldown.CooldownItem}
 * {@link github.rainbowmori.rainbowapi.object.cutomitem.DurabilityItem}
 * {@link github.rainbowmori.rainbowapi.object.cutomitem.CoolityItem}
 */
public interface InterfaceItem {

  // カスタムアイテムのnbtのpathです
  String nbtKey = "customitem";

  /**
   * アイテムの識別子を取得します
   * 
   * @return 識別子
   */
  @NotNull
  String getIdentifier();

  /**
   * このカスタムアイテムのインスタンスで使用しているアイテムを返します
   * 
   * @return itemstack
   */
  @NotNull
  ItemStack getItem();

  /**
   * {@link #heldItem()} 中にアクションバーにメッセージを送る文字列を返します
   * {@link Optional#empty()} の場合はメッセージを送りません
   * 
   * @param player プレイヤーです
   * @return 文字列のoptionalです
   */
  @NotNull
  Optional<String> getActionBarMessage(Player player);

  /**
   * {@link CustomItem#heldOfThis(PlayerItemHeldEvent)} で開始してほかのアイテムに変えた場合に終了する
   * アイテムを持っている間だけ1tickごとにする処理です
   * 
   * @return 処理
   */
  @NotNull
  default Optional<Consumer<Player>> heldItem() {
    return Optional.of(player -> {
      getActionBarMessage(player).ifPresent(s -> player.sendActionBar(Util.mm(s)));
      runTick().ifPresent(consumer -> consumer.accept(player));
    });
  }

  /**
   * {@link #heldItem()} を実行中に何かをしたい場合にこれを使用してください
   * 
   * @return 処理
   */
  @NotNull
  default Optional<Consumer<Player>> runTick() {
    return Optional.empty();
  }
}
