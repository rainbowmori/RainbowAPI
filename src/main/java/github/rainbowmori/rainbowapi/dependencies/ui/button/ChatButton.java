package github.rainbowmori.rainbowapi.dependencies.ui.button;

import github.rainbowmori.rainbowapi.dependencies.ui.menu.MenuHolder;
import github.rainbowmori.rainbowapi.util.ItemBuilder;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * クリックしたプレイヤーにチャットでメッセージを言わせるボタンです。
 *
 * @param <MH> the menu holder type
 */
public class ChatButton<MH extends MenuHolder<?>> extends ItemButton<MH> {

  private String message;

  /**
   * 定数でないチャットメッセージを使用したい ChatButtons のためのプロテクテッドコンストラクタです。
   * このコンストラクタを使用するサブクラスは、{@link #getMessage()}または{@link #getMessage(MenuHolder,
   * InventoryClickEvent)}のいずれかをオーバーライドしなければならない。
   *
   * @param icon the icon
   */
  protected ChatButton(ItemStack icon) {
    super(icon);
  }

  /**
   * ChatButtonを作成します。
   *
   * @param icon    the icon
   * @param message the chat message
   */
  public ChatButton(ItemStack icon, String message) {
    super(icon);
    setMessage(message);
  }

  /**
   * ChatButtonを作成します。アイコンの表示名は、メッセージに設定されます。
   *
   * @param material the icon material
   * @param message  the chat message
   */
  public ChatButton(Material material, String message) {
    this(new ItemBuilder(material).name(message).build(), message);
  }

  /**
   * ボタンをクリックしたプレイヤーに、チャットでメッセージを言わせる。
   *
   * @param menuHolder the menu holder
   * @param event      the InventoryClickEvent
   */
  @Override
  public void onClick(MH menuHolder, InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player player) {
      player.chat(getMessage(menuHolder, event));
    }
  }

  /**
   * {@link #onClick(MenuHolder, InventoryClickEvent)}によって送信されるチャットメッセージを計算します。
   * サブクラスは、一定でないチャットメッセージのためにこのメソッドをオーバーライドすることができます。
   * デフォルトの実装では、{@link #getMessage()}に委譲されます。
   *
   * @param menuHolder the menu holder
   * @param event      the InventoryClickEvent
   * @return the customized message
   */
  protected String getMessage(MH menuHolder, InventoryClickEvent event) {
    return getMessage();
  }

  /**
   * Get the chat message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * チャットメッセージを設定します。
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = Objects.requireNonNull(message, "Message cannot be null");
  }

}
