package github.rainbowmori.rainbowapi.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YamlAPI {
    public final File file;
    protected final String name, path;
    private final Plugin plugin;
    public FileConfiguration yaml;

    public YamlAPI(@NotNull Plugin plugin, String name) {
        this(plugin, name, "");
    }

    public YamlAPI(@NotNull Plugin plugin, String name, String path) {
        this.name = Objects.requireNonNullElse(name, "");
        this.path = Objects.requireNonNullElse(path, "");
        this.plugin = plugin;
        file = new File(plugin.getDataFolder() + this.path, this.name);
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
            plugin.getLogger().info(name + "を削除しました");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public final void Created() {
        try {
            if (!new File(plugin.getDataFolder() + path).exists()) {
                new File(plugin.getDataFolder() + path).mkdir();
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
