package github.rainbowmori.rainbowapi.object.customblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.api.JsonAPI;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class CustomBlockData extends JsonAPI {

  public CustomBlockData(Plugin plugin) {
    super(plugin, "customBlockData");
    if (!data.has("blocks")) {
      data.add("blocks", new JsonArray());
    }
    data.getAsJsonArray("blocks").forEach(jsonElement -> {
      JsonObject object = jsonElement.getAsJsonObject();
      Location loc = RainbowAPI.gson.fromJson(object.getAsJsonObject("location"), Location.class);
      String identifier = object.get("identifier").getAsString();
      JsonObject blockData = object.getAsJsonObject("data");
      if (!CustomBlock.isRegister(identifier)) {
        RainbowAPI.getPlugin().getPrefixUtil().logError(identifier + "はCustomBlockに登録されていませんのでロードされませんでした");
        return;
      }
      CustomBlock.loadCustomBlock(identifier, loc, blockData);
    });
  }

  @Override
  public JsonElement getSavaData() {
    JsonObject object = new JsonObject();
    JsonArray data = new JsonArray();
    CustomBlock.getBlocks().forEach((location, customBlock) -> {
      JsonObject json = new JsonObject();
      json.add("location", RainbowAPI.gson.toJsonTree(location));
      json.addProperty("identifier", customBlock.getIdentifier());
      json.add("data", customBlock.getBlockData());
      customBlock.clearData(customBlock.getLocation());
      data.add(json);
    });
    object.add("blocks", data);
    return object;
  }

}
