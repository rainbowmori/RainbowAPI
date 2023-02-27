package github.rainbowmori.rainbowapi.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import github.rainbowmori.rainbowapi.RainbowAPI;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class JsonAPI extends FileAPI<JsonObject> {
	
	public JsonAPI(RainbowAPI api, String name) {
		super(api, name);
	}
	
	public JsonAPI(RainbowAPI api, String name, String path) {
		super(api, name, path);
	}
	
	public static JsonElement convertElement(Object obj) {
		return obj instanceof JsonElement ? ((JsonElement) obj) : JsonParser.parseString(RainbowAPI.gson.toJson(obj));
	}
	
	private static String lastValue(List<Object> list) {
		return list.get(list.size() - 1).toString();
	}
	
	public final void ReCreate() {
		data = new JsonObject();
	}
	
	public final JsonObject getObject(@NotNull Object... keys) {
		return getObject(Arrays.stream(keys).toList());
	}
	
	public final JsonObject getObject(@NotNull List<Object> paths) {
		JsonObject obj = get(paths);
		String last = lastValue(paths);
		return obj != null && obj.has(last) ? obj.getAsJsonObject(last) : new JsonObject();
	}
	
	public final boolean hasElement(@NotNull Object... keys) {
		return get(Arrays.stream(keys).toList()).has(keys[keys.length - 1].toString());
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
		if (!jsonObject.has(lastPath)) {
			jsonObject.add(lastPath, new JsonArray());
		}
		JsonArray array = jsonObject.getAsJsonArray(lastPath);
		array.add(convertElement(object));
	}
	
	private JsonObject get(@NotNull List<Object> paths) {
		JsonObject jsonObject = data;
		if (paths.size() == 1) {
			return jsonObject;
		}
		for (int i = 0; i < paths.size() - 1; i++) {
			String key = paths.get(i).toString();
			if (!jsonObject.has(key)) {
				jsonObject.add(key, new JsonObject());
			}
			jsonObject = jsonObject.getAsJsonObject(key);
		}
		return jsonObject;
	}
	
	@Override
	protected String getExtension() {
		return ".json";
	}
	
	@Override
	public void Load() {
		try {
			data = RainbowAPI.gson.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
		} catch (FileNotFoundException ignored) {
		} finally {
			if (data == null) {
				data = new JsonObject();
			}
		}
	}
	
	@Override
	public void Save() {
		try (Writer writer = new FileWriter(file)) {
			RainbowAPI.gson.toJson(data, writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void FileLoad() {
		try {
			file.createNewFile();
			RainbowAPI.gson.toJson("{}", new FileWriter(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
