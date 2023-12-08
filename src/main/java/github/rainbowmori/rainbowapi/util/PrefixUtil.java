package github.rainbowmori.rainbowapi.util;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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
    log(getLoggerPrefix().append(Util.mm(message)), isInfoEnabled());
  }

  /**
   * consoleに[prefix + "DEBUG" + message] を送信します
   * 
   * @param message 送信するメッセージ
   */
  public final void logWarn(Object message) {
    log(loggerPrefix.append(getWarn()).append(Util.mm(message)), isWarnEnabled());
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
    log(loggerPrefix.append(getTrace()).append(Util.mm(message)), isTraceEnabled());
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
    log(loggerPrefix.append(getDebug()).append(Util.mm(message)), isDebugEnabled());
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
    log(loggerPrefix.append(getError()).append(Util.mm(message)), isErrorEnabled());
  }

  protected Component getError() {
    return Util.mm(" <reset><red>[ERROR]<reset> ");
  }

  /**
   * {@link #logInfo(Object)}
   * 
   * @return infoを許可するかどうか (default = true)
   */
  public boolean isInfoEnabled() {
    return true;
  }

  /**
   * {@link #logWarn(Object)}
   * 
   * @return warnを許可するかどうか (default = true)
   */
  public boolean isWarnEnabled() {
    return true;
  }

  /**
   * {@link #logTrace(Object)}
   * 
   * @return traceを許可するかどうか (default = true)
   */
  public boolean isTraceEnabled() {
    return true;
  }

  /**
   * {@link #logDebug(Object)}
   * 
   * @return debugを許可するかどうか (default = true)
   */
  public boolean isDebugEnabled() {
    return true;
  }

  /**
   * {@link #logError(Object)}
   * 
   * @return errorを許可するかどうか (default = true)
   */
  public boolean isErrorEnabled() {
    return true;
  }

  public final void send(UUID uuid, Object str) {
    send(Bukkit.getPlayer(uuid), str);
  }

  public final void send(CommandSender sender, Object str) {
    if (sender != null && isSendEnabled()) {
      sender.sendMessage(Util.mm(getPrefix()).append(Util.mm(str)));
    }
  }

  /**
   * {@link #send}
   * 
   * @return sendを許可するかどうか
   */
  public boolean isSendEnabled() {
    return true;
  }

  public final void Cast(String str) {
    if (isCastEnabled()) {
      Bukkit.broadcast(getPrefix().append(Util.mm(str)));
    }
  }

  /**
   * {@link #Cast(String)}
   * 
   * @return castを許可するかどうか
   */
  public boolean isCastEnabled() {
    return true;
  }

  public final Component getPrefix() {
    return prefix;
  }

  public final Component getLoggerPrefix() {
    return loggerPrefix;
  }
}
