package github.rainbowmori.rainbowapi.api;

import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YamlAPI {
    public final File file;
    protected final String name, path;
    private final RainbowAPI api;
    public FileConfiguration yaml;

    public YamlAPI(RainbowAPI api, String name) {
        this(api, name, "");
    }

    public YamlAPI(RainbowAPI api, String name, String path) {
        Objects.requireNonNull(name);
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.path = path == null || path.isEmpty() ? "" : "/" + path;
        this.api = api;
        file = new File(api.plugin.getDataFolder() + this.path, this.name);
        Created();
    }

    public final void Load() {
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public final void Save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final void Remove() {
        if (file.delete()) {
            api.mcUtil.logInfo(name + "を削除しました");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public final void Created() {
        try {
            if (!new File(api.plugin.getDataFolder() + path).exists()) {
                new File(api.plugin.getDataFolder() + path).mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
