package github.rainbowmori.rainbowapi.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.bukkit.plugin.Plugin;

/**
 * .properties ファイルを読み込む {@link FileAPI}
 */
public class PropertyAPI extends FileAPI<Properties> {


  /**
   * 引数のプラグインのフォルダーの第一階層からnameのファイルを読み込みます
   *
   * @param plugin 読み込みたいフォルダーのプラグイン
   * @param name   読み込むファイルの名前
   */
  public PropertyAPI(Plugin plugin, String name) {
    super(plugin, name);
  }

  /**
   * 引数のプラグインのフォルダーの {@link #path} {@link #name} のファイルを読み込みます 例 [plugin=TEST] [path=first/second]
   * [path=fileName] -> TEST/first/second/fileName.properties のファイルを読み込みます
   *
   * @param plugin 読み込みたいフォルダーのプラグイン
   * @param name   読み込むファイルの名前
   * @param path   ファイル階層
   */
  public PropertyAPI(Plugin plugin, String name, String path) {
    super(plugin, name, path);
  }

  /**
   * @return ファイルの拡張子
   */
  @Override
  protected String getExtension() {
    return ".properties";
  }

  /**
   * ファイルからデータの読み込み
   */
  @Override
  public void loadData() {
    try (InputStream input = new FileInputStream(file)) {
      Properties prop = new Properties();
      prop.load(input);
      this.data = prop;
    } catch (IOException e) {
      message.logError("Failed to load property file: " + paths);
      throw new RuntimeException(e);
    }
  }

  /**
   * データをファイルに保存
   */
  @Override
  public void saveFile() {
    try (OutputStream output = new FileOutputStream(file)) {
      this.data.store(output, "Properties file for " + plugin.getName());
    } catch (IOException e) {
      message.logError("Failed to save property file: " + paths);
      throw new RuntimeException(e);
    }
  }

  /**
   * プロパティファイルから値を取得する
   *
   * @param key キー
   * @return プロパティ値
   */
  public String getProperty(String key) {
    return this.data.getProperty(key);
  }

  /**
   * プロパティファイルに値を設定する
   *
   * @param key   キー
   * @param value プロパティ値
   */
  public void setProperty(String key, String value) {
    this.data.setProperty(key, value);
    saveFile();
  }

}
