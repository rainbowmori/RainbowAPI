package github.rainbowmori.rainbowapi.api;

import com.google.common.base.Charsets;
import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;

public class ConfigAPI extends YmlAPI {
	
	public ConfigAPI(RainbowAPI api, String name) {
		super(api, name);
	}
	
	public ConfigAPI(RainbowAPI api, String name, String path) {
		super(api, name, path);
	}
	
	@Override
	public void FileLoad() {
		api.plugin.saveResource(paths, true);
		FileConfiguration newConfig = YamlConfiguration.loadConfiguration(file);
		newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(api.plugin.getResource(paths), Charsets.UTF_8)));
	}
}
