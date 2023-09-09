package github.rainbowmori.rainbowapi.object.cutomitem.cooldown;

import java.util.Objects;

public final class ItemCooldown {

  private final String identifier;
  private final Long currentTime;
  private final double cooldown;

  public ItemCooldown(String identifier, double cooldown) {
    this.identifier = identifier;
    this.currentTime = System.currentTimeMillis();
    this.cooldown = cooldown;
  }

  public boolean isCooldown() {
    return (double) (System.currentTimeMillis() - currentTime()) / 1000 < cooldown();
  }

  public double getCooldown() {
    if (!isCooldown()) {
      return 0;
    }
    return cooldown() - (double) (System.currentTimeMillis() - currentTime()) / 1000 + 1;
  }

  public String identifier() {
    return identifier;
  }

  public Long currentTime() {
    return currentTime;
  }

  public double cooldown() {
    return cooldown;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (ItemCooldown) obj;
    return Objects.equals(this.identifier, that.identifier) &&
        Objects.equals(this.currentTime, that.currentTime) &&
        Double.doubleToLongBits(this.cooldown) == Double.doubleToLongBits(that.cooldown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, currentTime, cooldown);
  }

  @Override
  public String toString() {
    return "ItemCooldown[" +
        "identifier=" + identifier + ", " +
        "currentTime=" + currentTime + ", " +
        "cooldown=" + cooldown + ']';
  }

}
