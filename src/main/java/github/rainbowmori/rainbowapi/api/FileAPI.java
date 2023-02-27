package github.rainbowmori.rainbowapi.api;

import github.rainbowmori.rainbowapi.RainbowAPI;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class FileAPI<T> {
	
	public final File file;
	public final String name, path, paths;
	protected final RainbowAPI api;
	protected T data;
	
	public FileAPI(RainbowAPI api, String name) {
		this(api, name, null);
	}
	
	public FileAPI(RainbowAPI api, String name, String path) {
		Objects.requireNonNull(name);
		this.name = name.endsWith(getExtension()) ? name : name + getExtension();
		this.path = path == null || path.isEmpty() ? "" : "/" + path;
		this.paths = path == null || path.isEmpty() ? name : path + "/" + name;
		this.api = api;
		file = new File(api.plugin.getDataFolder() + this.path, this.name);
		Created();
	}
	
	public static void folderClear(File folder) {
		if (folder.exists()) {
			if (folder.isFile()) {
				folder.delete();
			} else if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				if (files != null && files.length != 0) {
					for (File file : files) {
						folderClear(file);
					}
				}
				folder.delete();
			}
		}
	}
	
	protected abstract String getExtension();
	
	public abstract void Load();
	
	public abstract void Save();
	
	public final void Remove() {
		if (file.delete()) {
			api.prefixUtil.logInfo(paths + "を削除しました");
		}
	}
	
	public void FileLoad() {
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public final void Created() {
		File directory = new File(api.plugin.getDataFolder() + path);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		if (!file.exists()) {
			FileLoad();
		}
		Load();
		Save();
	}
	
	public T getData() {
		return data;
	}
	
}
