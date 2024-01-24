package github.rainbowmori.rainbowapi.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.kyori.adventure.text.Component;

/**
 * {@link #prefix} を先頭につけメッセージを送信するUtil
 */
public class PrefixUtil {

  private final Component prefix;
  private final Component loggerPrefix;

  public PrefixUtil(Object prefix) {
    this(prefix, prefix);
  }

  public PrefixUtil(Object prefix, Object loggerPrefix) {
    this.prefix = Util.mm(prefix);
    this.loggerPrefix = Util.mm(loggerPrefix);
  }

  /**
   * consoleにmessageを送信します
   * 
   * @param message 送信するメッセージ
   */
  protected final void log(Object message) {
    Bukkit.getConsoleSender().sendMessage(Util.mm(message));
  }

  /**
   * 引数のbooleanに寄って送信するかを選択します
   * 
   * @param message  送信するメッセージ
   * @param loggable メッセージを送信するか
   */
  protected final void log(Object message, boolean loggable) {
    if (loggable) {
      log(message);
    }
  }

  /**
   * consoleにmessageを送信しますが {@link #isInfoEnabled()}
   * がfalseの場合は送信しません
   * 
   * @param message 送信するメッセージ
   */
  public final void logInfo(Object message) {
    log(getLoggerPrefix().append(Util.mm(message)));
  }

  /**
   * consoleに[prefix + "DEBUG" + message] を送信します
   * 
   * @param message 送信するメッセージ
   */
  public final void logWarn(Object message) {
    log(loggerPrefix.append(getWarn()).append(Util.mm(message)));
  }

  protected Component getWarn() {
    return Util.mm(" <reset><yellow>[WARN]<reset> ");
  }

  /**
   * consoleに[prefix + "TRACE" + message] を送信します
   * 
   * @param message 送信するメッセージ
   */
  public final void logTrace(Object message) {
    log(loggerPrefix.append(getTrace()).append(Util.mm(message)));
  }

  protected Component getTrace() {
    return Util.mm(" <reset><white>[TRACE]<reset> ");
  }

  /**
   * consoleに[prefix + "DEBUG" + message] を送信します
   * 
   * @param message 送信するメッセージ
   */
  public final void logDebug(Object message) {
    log(loggerPrefix.append(getDebug()).append(Util.mm(message)));
  }

  protected Component getDebug() {
    return Util.mm(" <reset><gray>[DEBUG]<reset> ");
  }

  /**
   * consoleに[prefix + "ERROR" + message] を送信します
   * 
   * @param message 送信するメッセージ
   */
  public final void logError(Object message) {
    log(loggerPrefix.append(getError()).append(Util.mm(message)));
  }

  protected Component getError() {
    return Util.mm(" <reset><red>[ERROR]<reset> ");
  }

  public final void send(UUID uuid, Object str) {
    send(Bukkit.getPlayer(uuid), str);
  }

  public final void send(CommandSender sender, Object str) {
    if (sender != null) {
      sender.sendMessage(Util.mm(getPrefix()).append(Util.mm(str)));
    }
  }

  public final void cast(Object str) {
    Bukkit.broadcast(getPrefix().append(Util.mm(str)));
  }

  public final void important(Object str) {
    Util.important(Util.mm(getPrefix()).append(Util.mm(str)));
  }

  public final Component getPrefix() {
    return prefix;
  }

  public final Component getLoggerPrefix() {
    return loggerPrefix;
  }
}
