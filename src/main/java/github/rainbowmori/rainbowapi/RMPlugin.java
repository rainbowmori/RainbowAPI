package github.rainbowmori.rainbowapi;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import github.rainbowmori.rainbowapi.object.customblock.CustomBlock;
import github.rainbowmori.rainbowapi.object.cutomitem.CustomItem;
import github.rainbowmori.rainbowapi.util.PrefixUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RMPlugin extends JavaPlugin {

  /**
   * {@link CustomItem#register(String, Class)},{@link CustomBlock#register(String, Class)} を
   * {@link RainbowAPI} が完全にロードされてから読み込むための一時保存先
   */
  static final Map<String, Class<? extends CustomItem>> customItems = new HashMap<>();
  static final Map<String, Class<? extends CustomBlock>> customBlocks = new HashMap<>();

  //CommandAPIで登録されたコマンドまとめ
  static final Set<String> registeredCommands = new HashSet<>();

  //プラグイン独自の PrefixUtil
  protected final PrefixUtil prefixUtil;

  /**
   * 初期化
   */
  public RMPlugin() {
    super();
    prefixUtil = new PrefixUtil(getPrefix());
  }

  /**
   * プラグイン起動時に {@link CustomItem} の登録をする場合はこちらを使用してください
   *
   * @param identifier CustomItemの識別子
   * @param clazz      登録するCustomItemのクラス
   */
  public void registerItem(String identifier, Class<? extends CustomItem> clazz) {
    customItems.put(identifier, clazz);
  }

  /**
   * プラグイン起動時に {@link CustomBlock} の登録をする場合はこちらを使用してください
   *
   * @param identifier CustomBlockの識別子
   * @param clazz      登録するCustomBlockのクラス
   */
  public void registerBlock(String identifier, Class<? extends CustomBlock> clazz) {
    customBlocks.put(identifier, clazz);
  }

  /**
   * {@link #prefixUtil} で使用するprefix
   */
  public abstract String getPrefix();

  /**
   * {@link CommandAPICommand} を登録するメゾット
   *
   * @param commandAPICommand instance
   */
  public void registerCommand(CommandAPICommand commandAPICommand) {
    registeredCommands.add(commandAPICommand.getName());
    commandAPICommand.register();
    getPrefixUtil().logDebug("<blue>REGISTER COMMAND " + commandAPICommand.getName());
  }

  /**
   * {@link CommandTree} を登録するメゾット
   *
   * @param commandTree instance
   */
  public void registerCommand(CommandTree commandTree) {
    registeredCommands.add(commandTree.getName());
    commandTree.register();
    getPrefixUtil().logDebug("<blue>REGISTER COMMAND " + commandTree.getName());
  }

  /**
   * {@link Listener} を登録するメゾット
   *
   * @param listener instance
   */
  public void registerEvent(Listener listener) {
    this.getServer().getPluginManager().registerEvents(listener, this);
  }

  /**
   * {@link PrefixUtil#PrefixUtil(Object)} new PrefixUtil({@link #getPrefix()})
   *
   * @return {@link #prefixUtil}
   */
  public PrefixUtil getPrefixUtil() {
    return prefixUtil;
  }

}
