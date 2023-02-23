package github.rainbowmori.rainbowapi.api;

import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YamlAPI {
    public final File file;
    public final String name, path, paths;
    protected final RainbowAPI api;
    protected FileConfiguration yaml;

    public YamlAPI(RainbowAPI api, String name) {
        this(api, name, "");
    }

    public YamlAPI(RainbowAPI api, String name, String path) {
        Objects.requireNonNull(name);
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.path = path == null || path.isEmpty() ? "" : "/" + path;
        this.paths = path == null || path.isEmpty() ? name : path + "/" + name;
        this.api = api;
        file = new File(api.plugin.getDataFolder() + this.path, this.name);
        Created();
    }

    public final void Load() {
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void Save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Remove() {
        if (file.delete()) {
            api.prefixUtil.logInfo(name + "を削除しました");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void Created() {
        try {
            if (!new File(api.plugin.getDataFolder() + path).exists()) {
                new File(api.plugin.getDataFolder() + path).mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public FileConfiguration getYaml() {
        return yaml;
    }
    
}
