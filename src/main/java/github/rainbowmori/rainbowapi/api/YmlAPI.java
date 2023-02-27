package github.rainbowmori.rainbowapi.api;

import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class YmlAPI extends FileAPI<FileConfiguration>{
	
	public YmlAPI(RainbowAPI api, String name) {
		super(api, name);
	}
	
	public YmlAPI(RainbowAPI api, String name, String path) {
		super(api, name, path);
	}
	
	@Override
	protected String getExtension() {
		return ".yml";
	}
	
	@Override
	public void Load() {
		data = YamlConfiguration.loadConfiguration(file);
	}
	
	@Override
	public void Save() {
		try {
			data.save(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
