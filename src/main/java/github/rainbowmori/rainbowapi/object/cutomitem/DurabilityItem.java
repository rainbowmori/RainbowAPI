package github.rainbowmori.rainbowapi.object.cutomitem;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * カスタムアイテムに耐久値の概念を持たす機能クラスです
 * customitem/durabilityに耐久値が保存されます
 */
public interface DurabilityItem extends InterfaceItem {

  /**
   * 耐久を消費するメゾットです
   * <p>
   * </p>
   * <p>
   * if(useDurability(Integer)){
   * </p>
   * <p>
   * itemUse()
   * </p>
   * <p>
   * }
   * </p>
   *
   * をすることで耐久値をすべて消費した際に一つアイテムを消費するする処理ができます
   *
   * @param number 消費する耐久量
   * @return 耐久をすべて使ったらtrueを返します
   */
  default boolean useDurability(int number) {
    return NBT.modify(getItem(), nbt -> {
      ReadWriteNBT compound = nbt.getOrCreateCompound(nbtKey);
      int durability = getDurability();
      int i = durability - number;
      if (i > 0) {
        compound.setInteger("durability", i);
        return false;
      }
      compound.removeKey("durability");
      return true;
    });
  }

  /**
   * 現在のアイテムの耐久を取得します
   * 
   * @return durability
   */
  default int getDurability() {
    return NBT.modify(getItem(), nbt -> {
      ReadWriteNBT compound = nbt.getOrCreateCompound(nbtKey);
      if (compound.hasTag("durability")) {
        return compound.getInteger("durability");
      } else {
        compound.setInteger("durability", getMaxDurability());
        return getMaxDurability();
      }
    });
  }

  @Override
  default @NotNull Optional<String> getActionBarMessage(Player player) {
    return Optional.ofNullable(getDurabilityMessage(player.getUniqueId()));
  }

  /**
   * クールダウン中に送るメッセージです
   * 
   * @param uuid プレイヤーのuuidです
   * @return 文字列です
   */

  default String getDurabilityMessage(UUID uuid) {
    return "<gray>(<aqua>%s<white>/<green>%s<gray>)".formatted(getDurability(), getMaxDurability());
  }

  /**
   * マックスの耐久値です
   * 
   * @return integer
   */
  int getMaxDurability();
}
