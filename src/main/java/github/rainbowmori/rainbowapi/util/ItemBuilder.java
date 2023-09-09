package github.rainbowmori.rainbowapi.util;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * アイテムを作成やmodifyするクラス
 */
@SuppressWarnings("unused")
public class ItemBuilder {

  private final ItemStack itemStack;

  public ItemBuilder(Material material) {
    this.itemStack = new ItemStack(Objects.requireNonNull(material, "Material cannot be null"));
  }

  public ItemBuilder(ItemStack itemStack) {
    Objects.requireNonNull(itemStack, "ItemStack cannot be null");
    this.itemStack = itemStack;
  }

  /**
   * プレイヤーの頭を作成するクラス
   * @param skullOwner プレイヤーの名前
   * @return 作成したアイテムスタック
   */
  public static ItemStack createSkull(String skullOwner) {
    return new ItemBuilder(Material.PLAYER_HEAD).changeMeta(
        (Consumer<SkullMeta>) skullMeta -> skullMeta.setOwner(skullOwner)).build();
  }

  /**
   * アイテムの数を変更します
   * @param amount 変更する量
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder amount(int amount) {
    return change(i -> i.setAmount(amount));
  }

  /**
   * エンチャントを設定
   * @param enchantment 追加するエンチャント
   * @param level エンチャントのレベル (6とか100とかでもok)
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder enchant(Enchantment enchantment, int level) {
    return change(i -> i.addUnsafeEnchantment(enchantment, level));
  }

  /**
   * エンチャントを削除
   * @param enchantment 削除するエンチャント
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder unEnchant(Enchantment enchantment) {
    return change(i -> i.removeEnchantment(enchantment));
  }

  /**
   * 耐久値のダメージを設定
   * @param damage ダメージ
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder damage(int damage) {
    return changeItemMeta(meta -> ((Damageable) meta).setDamage(damage));
  }

  /**
   * 耐久値の設定
   * @param damage 耐久値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder durability(int damage) {
    return changeItemMeta(meta -> ((Damageable) meta).setDamage(Math.abs(itemStack.getType().getMaxDurability()-damage)));
  }

  /**
   * マテリアルの変更
   * @param type 変更するマテリアル
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder type(Material type) {
    return change(i -> i.setType(type));
  }

  /**
   * アイテムの名前を変更
   * @param displayName 変更するアイテム名
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder name(Object displayName) {
    return changeItemMeta(meta -> meta.displayName(Util.mm(displayName)));
  }

  /**
   * loreの変更
   * @param lore 変更するlore
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder lore(@NotNull List<?> lore) {
    return changeItemMeta(meta -> meta.lore(Util.ListMM(lore)));
  }

  /**
   * loreの変更
   * @param lore 変更するlore
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder lore(@NotNull Object... lore) {
    return lore(Arrays.asList(lore));
  }

  /**
   * loreの追加
   * @param str 追加するloreの文
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder addLore(Object str) {
    List<Component> lore = getLore();
    lore.add(Util.mm(str));
    return lore(lore);
  }

  /**
   * loreの {@code line} 行目に {@code str} を挿入します
   * @param line loreの行 (0の場合は一番最初に入ります)
   * @param str 挿入するloreの文
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder insertLore(int line, Object str) {
    List<Component> lore = getLore();
    lore.add(line, Util.mm(str));
    return lore(lore);
  }

  /**
   * loreの {@code line} 行目に {@code str} を上書きします
   * @param line 上書きする行
   * @param str 上書きする文
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder setLore(int line, Object str) {
    List<Component> lore = getLore();
    while (lore.size() <= line) {
      lore.add(Util.mm(" "));
    }
    lore.set(line, Util.mm(str));
    return lore(lore);
  }

  /**
   * loreから {@code line} 行目 を削除
   * @param line 削除する行
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder removeLore(int line) {
    List<Component> lore = getLore();
    lore.remove(line);
    return lore(lore);
  }

  /**
   * {@link #lore(List)} を使用するために簡単にloreを取得できるように
   * @return このアイテムのlore
   */
  public List<Component> getLore() {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta.hasLore()) {
      return itemMeta.lore();
    }
    List<Component> lore = new ArrayList<>();
    itemMeta.lore(lore);
    return lore;
  }

  /**
   * アイテムをエンチャントで光らせます
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder glow() {
    return enchant(Enchantment.LURE, 1).flags(ItemFlag.HIDE_ENCHANTS);
  }

  /**
   * アイテムを {@link #glow()} で光らせてる場合に削除します
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder unGlow() {
    return unEnchant(Enchantment.LURE).unFlags(ItemFlag.HIDE_ENCHANTS);
  }

  /**
   * アイテムを壊せるかどうか設定します
   * @param unbreakable 壊せるかどうか (true = 不可壊)
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder unbreakable(boolean unbreakable) {
    return changeItemMeta(meta -> meta.setUnbreakable(unbreakable));
  }

  /**
   * {@link ItemFlag} をアイテムに追加します
   * @param flags 追加するフラッグたち
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder flags(ItemFlag... flags) {
    return changeItemMeta(meta -> meta.addItemFlags(flags));
  }

  /**
   * {@link ItemFlag} をアイテムから削除します
   * @param flags 削除するフラッグたち
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder unFlags(ItemFlag... flags) {
    return changeItemMeta(meta -> meta.removeItemFlags(flags));
  }

  /**
   * {@link ItemMeta#setAttributeModifiers(Multimap)} を見てください
   * @param attributeModifiers arg
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder attributes(Multimap<Attribute, AttributeModifier> attributeModifiers) {
    return changeItemMeta(meta -> meta.setAttributeModifiers(attributeModifiers));
  }

  /**
   * {@link Attribute} を追加します
   * @param attribute 追加する対象
   * @param attributeModifier 詳細設定
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
    return changeItemMeta(meta -> meta.addAttributeModifier(attribute, attributeModifier));
  }

  /**
   * マップで {@link #addAttribute(Attribute, AttributeModifier)} をできるように
   * @param attributeModifiers arg
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder addAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers) {
    return attributeModifiers.entries().stream().reduce(this,
        (itemBuilder, entry) -> itemBuilder.addAttribute(entry.getKey(), entry.getValue()),
        (first, second) -> second);
  }

  /**
   * Entryで {@link #addAttribute(Attribute, AttributeModifier)} をできるように
   * @param attributeModifiers arg
   * @return new instance (use {@link #build()} to create)
   */
  @SafeVarargs
  public final ItemBuilder addAttributes(
      Map.Entry<Attribute, AttributeModifier>... attributeModifiers) {
    ItemBuilder result = this;
    for (var entry : attributeModifiers) {
      result = result.addAttribute(entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * カスタムモデルデータを設定します
   * @param customModelData 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder customModelData(Integer customModelData) {
    return changeItemMeta(meta -> meta.setCustomModelData(customModelData));
  }

  /**
   * NBTみたいなやつの変更するメゾット
   * @param consumer 実行する処理
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder changePersistentData(Consumer<? super PersistentDataContainer> consumer) {
    return changeItemMeta(meta -> consumer.accept(meta.getPersistentDataContainer()));
  }

  /**
   * 値の追加
   * @param key 値のキー
   * @param type 値をどのように扱うか {@link PersistentDataType}
   * @param value 保存する値
   * @return  new instance (use {@link #build()} to create)
   * @param <T> 指定されたタグに格納されているプライマリ オブジェクト タイプ
   * @param <Z> このタグ タイプを適用するときに取得されたオブジェクト タイプ
   */
  public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type,
      Z value) {
    return changePersistentData(data -> data.set(key, type, value));
  }

  /**
   * byte で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, byte value) {
    return persistentData(key, PersistentDataType.BYTE, value);
  }

  /**
   * byte[] で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, byte[] value) {
    return persistentData(key, PersistentDataType.BYTE_ARRAY, value);
  }

  /**
   * double で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, double value) {
    return persistentData(key, PersistentDataType.DOUBLE, value);
  }

  /**
   * float で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, float value) {
    return persistentData(key, PersistentDataType.FLOAT, value);
  }

  /**
   * int で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, int value) {
    return persistentData(key, PersistentDataType.INTEGER, value);
  }

  /**
   * int[]で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, int[] value) {
    return persistentData(key, PersistentDataType.INTEGER_ARRAY, value);
  }

  /**
   * long で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, long value) {
    return persistentData(key, PersistentDataType.LONG, value);
  }

  /**
   * long[] で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, long[] value) {
    return persistentData(key, PersistentDataType.LONG_ARRAY, value);
  }

  /**
   * short で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, short value) {
    return persistentData(key, PersistentDataType.SHORT, value);
  }

  /**
   * string で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, String value) {
    return persistentData(key, PersistentDataType.STRING, value);
  }

  /**
   * {@link PersistentDataContainer} で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, PersistentDataContainer value) {
    return persistentData(key, PersistentDataType.TAG_CONTAINER, value);
  }

  /**
   * {@link PersistentDataContainer[]} で {@link #persistentData(NamespacedKey, PersistentDataType, Object)} を設定します
   * @param key 値のキー
   * @param value 値
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder persistentData(NamespacedKey key, PersistentDataContainer[] value) {
    return persistentData(key, PersistentDataType.TAG_CONTAINER_ARRAY, value);
  }

  /**
   * {@link #createSkull(String)} のように先にItemMetaのキャストをしたいときに使用してください
   * @param consumer 実行する処理
   * @return new instance (use {@link #build()} to create)
   * @param <IM> ItemMeta の サブクラス
   */
  @SuppressWarnings("unchecked")
  public <IM extends ItemMeta> ItemBuilder changeMeta(Consumer<IM> consumer) {
    return changeItemMeta(m -> consumer.accept((IM) m));
  }

  /**
   * meta を変更する時に使用します
   * @param consumer 実行する処理
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder changeItemMeta(Consumer<? super ItemMeta> consumer) {
    return change(i -> {
      ItemMeta meta = i.getItemMeta();
      consumer.accept(meta);
      i.setItemMeta(meta);
    });
  }

  /**
   * アイテムを変更する際に使用します
   * @param consumer 変更する処理
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder change(Consumer<? super ItemStack> consumer) {
    ItemBuilder builder = new ItemBuilder(itemStack);
    consumer.accept(builder.itemStack);
    return builder;
  }

  /**
   * アイテム自体で処理する際に使用します
   * @param function 処理
   * @return new instance (use {@link #build()} to create)
   */
  public ItemBuilder map(Function<? super ItemStack, ? extends ItemStack> function) {
    return new ItemBuilder(function.apply(build()));
  }

  public ItemStack build() {
    return itemStack;
  }

}
