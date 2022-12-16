package github.rainbowmori.rainbowapi.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class JsonUtil {

    public static JsonElement encodeItemStack(ItemStack itemStack) {
        return new JsonPrimitive(Base64.getEncoder().encodeToString(itemStack.serializeAsBytes()));
    }

    public static ItemStack decodeItemStack(JsonElement data) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(data.getAsString()));
    }

    public static JsonElement encodeLocation(Location location) {
        JsonObject element = new JsonObject();
        location.serialize().forEach((s, o) -> element.addProperty(s, o.toString()));
        return element;
    }

    public static Location decodeLocation(JsonElement data) {
        JsonObject obj = data.getAsJsonObject();
        return new Location(Bukkit.getWorld(obj.get("world").getAsString()),
                obj.get("x").getAsDouble(), obj.get("y").getAsDouble(), obj.get("z").getAsDouble(),
                obj.get("yaw").getAsFloat(), obj.get("pitch").getAsFloat());
    }
}
