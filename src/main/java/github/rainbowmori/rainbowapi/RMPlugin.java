package github.rainbowmori.rainbowapi;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import github.rainbowmori.rainbowapi.util.PrefixUtil;

public abstract class RMPlugin extends JavaPlugin {

  // CommandAPIで登録されたコマンドまとめ
  static final Set<String> registeredCommands = new HashSet<>();

  // プラグイン独自の PrefixUtil
  protected final PrefixUtil prefixUtil;

  /**
   * 初期化
   */
  public RMPlugin() {
    super();
    prefixUtil = new PrefixUtil(getPrefix());
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
    getPrefixUtil().logDebug("<blue>REGISTER COMMAND " +
        commandAPICommand.getName());
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
