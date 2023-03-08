package github.rainbowmori.rainbowapi.util;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
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
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(Objects.requireNonNull(material, "Material cannot be null"));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack, "ItemStack cannot be null").clone();
    }
    
    public static ItemStack createSkull(String skullOwner) {
        return new ItemBuilder(Material.PLAYER_HEAD).changeMeta((Consumer<SkullMeta>) skullMeta -> skullMeta.setOwner(skullOwner)).build();
    }

    public ItemBuilder amount(int amount) {
        return change(i -> i.setAmount(amount));
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        return change(i -> i.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder unEnchant(Enchantment enchantment) {
        return change(i -> i.removeEnchantment(enchantment));
    }

    public ItemBuilder damage(int damage) {
        return changeMeta(meta -> ((Damageable) meta).setDamage(damage));
    }

    public ItemBuilder type(Material type) {
        return change(i -> i.setType(type));
    }

    public ItemBuilder name(Object displayName) {
        return changeMeta(meta -> meta.displayName(Util.mm(displayName)));
    }

    public ItemBuilder lore(List<?> lore) {
        return changeMeta(meta -> meta.lore(Util.ListMM(lore)));
    }

    public ItemBuilder lore(Object... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(Object str) {
        List<Component> lore = getLore();
        lore.add(Util.mm(str));
        return lore(lore);
    }
    
    public ItemBuilder insertLore(int line, Object str) {
        List<Component> lore = getLore();
        lore.add(line, Util.mm(str));
        return lore(lore);
    }

    public ItemBuilder setLore(int line, Object str) {
        List<Component> lore = getLore();
        while (lore.size() <= line){
            lore.add(Util.mm(" "));
        }
        lore.set(line, Util.mm(str));
        return lore(lore);
    }

    public ItemBuilder removeLore(int line) {
        List<Component> lore = getLore();
        lore.remove(line);
        return lore(lore);
    }

    public List<Component> getLore() {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ?
                new ArrayList<>(Objects.requireNonNull(itemStack.getItemMeta().lore())) : new ArrayList<>();
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return changeMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    public ItemBuilder flags(ItemFlag... flags) {
        return changeMeta(meta -> meta.addItemFlags(flags));
    }

    public ItemBuilder attributes(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        return changeMeta(meta -> meta.setAttributeModifiers(attributeModifiers));
    }

    @SafeVarargs
    public final ItemBuilder attributes(Map.Entry<Attribute, AttributeModifier>... attributeModifiers) {
        return attributes(ImmutableMultimap.copyOf(List.of(attributeModifiers)));
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        return changeMeta(meta -> meta.addAttributeModifier(attribute, attributeModifier));
    }

    public ItemBuilder addAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        return attributeModifiers.entries().stream().reduce(this, (itemBuilder, entry) -> itemBuilder.addAttribute(entry.getKey(), entry.getValue()), (first, second) -> second);
    }

    @SafeVarargs
    public final ItemBuilder addAttributes(Map.Entry<Attribute, AttributeModifier>... attributeModifiers) {
        ItemBuilder result = this;
        for (var entry : attributeModifiers) {
            result = result.addAttribute(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public ItemBuilder customModelData(Integer customModelData) {
        return changeMeta(meta -> meta.setCustomModelData(customModelData));
    }

    public ItemBuilder changePersistentData(Consumer<? super PersistentDataContainer> consumer) {
        return changeMeta(meta -> consumer.accept(meta.getPersistentDataContainer()));
    }


    public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        return changePersistentData(data -> data.set(key, type, value));
    }

    public ItemBuilder persistentData(NamespacedKey key, byte value) {
        return persistentData(key, PersistentDataType.BYTE, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, byte[] value) {
        return persistentData(key, PersistentDataType.BYTE_ARRAY, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, double value) {
        return persistentData(key, PersistentDataType.DOUBLE, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, float value) {
        return persistentData(key, PersistentDataType.FLOAT, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, int value) {
        return persistentData(key, PersistentDataType.INTEGER, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, int[] value) {
        return persistentData(key, PersistentDataType.INTEGER_ARRAY, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, long value) {
        return persistentData(key, PersistentDataType.LONG, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, long[] value) {
        return persistentData(key, PersistentDataType.LONG_ARRAY, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, short value) {
        return persistentData(key, PersistentDataType.SHORT, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, String value) {
        return persistentData(key, PersistentDataType.STRING, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, PersistentDataContainer value) {
        return persistentData(key, PersistentDataType.TAG_CONTAINER, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, PersistentDataContainer[] value) {
        return persistentData(key, PersistentDataType.TAG_CONTAINER_ARRAY, value);
    }

    @Deprecated
    public <T, Z> ItemBuilder tag(NamespacedKey key, ItemTagType<T, Z> type, Z value) {
        return changeMeta(meta -> meta.getCustomTagContainer().setCustomTag(key, type, value));
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, byte value) {
        return tag(key, ItemTagType.BYTE, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, byte[] value) {
        return tag(key, ItemTagType.BYTE_ARRAY, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, double value) {
        return tag(key, ItemTagType.DOUBLE, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, float value) {
        return tag(key, ItemTagType.FLOAT, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, int value) {
        return tag(key, ItemTagType.INTEGER, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, int[] value) {
        return tag(key, ItemTagType.INTEGER_ARRAY, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, long value) {
        return tag(key, ItemTagType.LONG, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, long[] value) {
        return tag(key, ItemTagType.LONG_ARRAY, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, short value) {
        return tag(key, ItemTagType.SHORT, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, String value) {
        return tag(key, ItemTagType.STRING, value);
    }

    @Deprecated
    public ItemBuilder tag(NamespacedKey key, CustomItemTagContainer value) {
        return tag(key, ItemTagType.TAG_CONTAINER, value);
    }

    @SuppressWarnings("unchecked")
    public <IM extends ItemMeta> ItemBuilder changeMeta(Consumer<IM> consumer) {
        return changeItemMeta(m -> consumer.accept((IM) m));
    }

    public ItemBuilder changeItemMeta(Consumer<? super ItemMeta> consumer) {
        return change(i -> {
            ItemMeta meta = i.getItemMeta();
            consumer.accept(meta);
            i.setItemMeta(meta);
        });
    }

    public ItemBuilder change(Consumer<? super ItemStack> consumer) {
        ItemBuilder builder = new ItemBuilder(itemStack);
        consumer.accept(builder.itemStack);
        return builder;
    }

    public ItemBuilder map(Function<? super ItemStack, ? extends ItemStack> function) {
        return new ItemBuilder(function.apply(build()));
    }

    public ItemStack build() {
        return itemStack.clone();
    }

}
