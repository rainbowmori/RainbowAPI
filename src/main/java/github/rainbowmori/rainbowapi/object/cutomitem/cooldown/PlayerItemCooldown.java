package github.rainbowmori.rainbowapi.object.cutomitem.cooldown;

import github.rainbowmori.rainbowapi.object.cache.CacheData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerItemCooldown implements CacheData<UUID> {

  private final UUID uuid;

  private final Map<String, ItemCooldown> cooldownMap = new HashMap<>();

  public PlayerItemCooldown(UUID uuid) {
    this.uuid = uuid;
  }

  public int getIntCooldown(String identifier) {
    if (!hasCooldown(identifier)) {
      return 0;
    }
    ItemCooldown cooldown = getCooldown(identifier);
    return (int) cooldown.getCooldown();
  }

  public double getDoubleCooldown(String identifier) {
    if (!hasCooldown(identifier)) {
      return 0;
    }
    ItemCooldown cooldown = getCooldown(identifier);
    return cooldown.getCooldown();
  }

  public void updateCooldown(String identifier) {
    if (!cooldownMap.containsKey(identifier)) {
      return;
    }
    ItemCooldown cooldown = cooldownMap.get(identifier);
    if (!cooldown.isCooldown()) {
      removeCooldown(identifier);
    }
  }

  public void removeCooldown(String identifier) {
    cooldownMap.remove(identifier);
  }

  public boolean hasCooldown(String identifier) {
    updateCooldown(identifier);
    return cooldownMap.containsKey(identifier);
  }

  public boolean addCooldown(ItemCooldown itemCooldown) {
    String identifier = itemCooldown.identifier();
    if (!hasCooldown(identifier)) {
      setCooldown(itemCooldown);
      return false;
    }
    setCooldown(new ItemCooldown(identifier,itemCooldown.getCooldown() + getCooldown(identifier).getCooldown()));
    return true;
  }

  public ItemCooldown getCooldown(String identifier) {
    if (hasCooldown(identifier)) {
      return cooldownMap.get(identifier);
    }
    return null;
  }

  public void setCooldown(ItemCooldown itemCooldown) {
    cooldownMap.put(itemCooldown.identifier(), itemCooldown);
  }

  @Override
  public UUID getKey() {
    return uuid;
  }
}
