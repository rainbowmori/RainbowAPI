package github.rainbowmori.rainbowapi.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JsonAPI {
    public final File file;
    protected final String name, path;
    private final Plugin plugin;
    public JsonObject json;

    public JsonAPI(Plugin plugin, String name) {
        this(plugin,name,"");
    }

    public JsonAPI(Plugin plugin, String name, String path) {
        Objects.requireNonNull(name);
        this.name = name.endsWith(".json") ? name : name + ".json";
        this.path = Objects.requireNonNullElse(path, "");
        this.plugin = plugin;
        file = new File(plugin.getDataFolder() + this.path, this.name);
        Created();
    }

    public final void ReCreate() {
        json = new JsonObject();
    }

    public final JsonObject getObject(@NotNull Object... keys) {
        return getObject(Arrays.stream(keys).toList());
    }

    public final JsonObject getObject(@NotNull List<Object> paths) {
        JsonObject obj = get(paths);
        String last = lastValue(paths);
        return obj.has(last) ? obj.getAsJsonObject(last) : new JsonObject();
    }

    public final boolean hasElement(@NotNull Object... keys) {
        return get(Arrays.stream(keys).toList()).has(keys[keys.length-1].toString());
    }

    public final boolean containsElement(Object object, @NotNull Object... keys) {
        return containsElement(object, Arrays.stream(keys).toList());
    }

    public final boolean containsElement(Object object, @NotNull List<Object> paths) {
        JsonObject obj = get(paths);
        String last = lastValue(paths);
        return obj.has(last) && obj.getAsJsonArray(last).contains(convertElement(object));
    }

    public final boolean equalsElement(Object object, @NotNull Object... paths) {
        return equalsElement(object, Arrays.stream(paths).toList());
    }

    public final boolean equalsElement(Object object, @NotNull List<Object> paths) {
        JsonObject obj = get(paths);
        String last = lastValue(paths);
        return obj.has(last) && obj.get(last).equals(object);
    }

    public final void writeSingle(Object object, @NotNull Object... paths) {
        writeSingle(object, Arrays.stream(paths).toList());
    }

    public final void writeSingle(Object object, @NotNull List<Object> paths) {
        get(paths).add(lastValue(paths), convertElement(object));
    }

    public final void writeArray(Object object, @NotNull Object... paths) {
        writeArray(object, Arrays.stream(paths).toList());
    }

    public final void writeArray(Object object, @NotNull List<Object> paths) {
        JsonObject jsonObject = get(paths);
        String lastPath = lastValue(paths);
        if (!jsonObject.has(lastPath)) jsonObject.add(lastPath, new JsonArray());
        JsonArray array = jsonObject.getAsJsonArray(lastPath);
        array.add(convertElement(object));
    }

    private static String lastValue(List<Object> list) {
        return list.get(list.size() - 1).toString();
    }

    private JsonObject get(@NotNull List<Object> paths) {
        JsonObject jsonObject = json;
        if (paths.size() == 1) return jsonObject;
        for (int i = 0; i < paths.size() - 1; i++) {
            String key = paths.get(i).toString();
            if (!jsonObject.has(key)) jsonObject.add(key, new JsonObject());
            jsonObject = jsonObject.getAsJsonObject(key);
        }
        return jsonObject;
    }

    public final JsonElement convertElement(Object obj) {
        return obj instanceof JsonElement ? ((JsonElement) obj) : RainbowAPI.gson.fromJson(String.valueOf(obj), JsonElement.class);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public final void Created() {
        try {
            if (!new File(plugin.getDataFolder() + path).exists())
                new File(plugin.getDataFolder() + path).mkdir();
            if (!file.exists() || new BufferedReader(new FileReader(file)).readLine() == null) {
                file.createNewFile();
                RainbowAPI.getPlugin().getLogger().info(name + "に{}を入力しています");
                FileWriter writer = new FileWriter(file);
                writer.write("{}");
                writer.close();
            }
            json = RainbowAPI.gson.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public final void Remove() {
        if (file.delete()) RainbowAPI.getPlugin().getLogger().info(name + "を削除しました");;
    }

    public final void Save() {
        try {
            FileWriter writer = new FileWriter(file);
            RainbowAPI.gson.toJson(json, writer);
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
