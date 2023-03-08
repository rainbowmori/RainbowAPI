package github.rainbowmori.rainbowapi.util;

import github.rainbowmori.rainbowapi.RMHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * よく使うメゾットがある
 */

public class Util {
	
	public static final PrefixUtil util = RMHome.getRainbowAPI().prefixUtil;
	
	public static Component mm(Object str) {
		return IsObjectUtil.IsComponent(str) ? ((Component) str) :
			MiniMessage.miniMessage().deserialize(String.valueOf(str)).decoration(TextDecoration.ITALIC, false);
	}
	
	public static void consoleCommand(String command) {
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	
	public static Component component(String str) {
		return Util.mm(vanillaToMM(str));
	}
	
	public static void send(UUID uuid, String str) {
		send(Bukkit.getPlayer(uuid), str);
	}
	
	public static void send(CommandSender sender, String str) {
		if (sender != null) {
			sender.sendMessage(Util.mm(str));
		}
	}
	
	public static void send(UUID uuid, Component str) {
		send(Bukkit.getPlayer(uuid), str);
	}
	
	public static void send(CommandSender sender, Component str) {
		if (sender != null) {
			sender.sendMessage(str);
		}
	}
	
	public static void executeCommand(Player execute, String command) {
		if (execute.isOp()) {
			execute.performCommand(command);
		} else {
			try {
				execute.setOp(true);
				execute.performCommand(command);
			} finally {
				execute.setOp(false);
			}
		}
	}
	
	public static String vanillaToMM(String str) {
		StringBuilder builder = new StringBuilder();
		char[] b = str.toCharArray();
		for (int i = 0; i < b.length-1; i++) {
			char c = b[i + 1];
			if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(c) > -1) {
				builder.append('<').append(ChatColor.getByChar(c).name().toLowerCase()).append('>');
				continue;
			}
			if (i-1 > -1 && b[i - 1] != '&') {
				builder.append(b[i]);
			}
		}
		if (b.length-2 > -1 && b[b.length-2] != '&') {
			builder.append(b[b.length-1]);
		}
		return builder.toString();
	}
	
	public static Component legacy(String str) {
		return LegacyComponentSerializer.legacyAmpersand().deserializeOrNull(str);
	}
	
	public static String cc(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String toAndChatColor(String str) {
		char[] b = str.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
				b[i] = '&';
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}
	
	/**
	 * listの内容をComponentに変換
	 *
	 * @param list 変換したいlist
	 *
	 * @return List<Component>
	 */
	
	public static List<Component> ListMM(List<?> list) {
		return list.stream().map(Util::mm).collect(Collectors.toList());
	}
	
	/**
	 * {@link Component}をStringに変換
	 *
	 * @param str 変換したいコンポーネント
	 *
	 * @return 変換されたString
	 */
	
	public static String serialize(Component str) {
		String serialize = MiniMessage.miniMessage().serialize(str);
		if (serialize.startsWith("\\u003c!italic\\u003e")) {
			return serialize.substring("\\u003c!italic\\u003e".length());
		}
		if (serialize.startsWith("<!italic>")) {
			return serialize.substring("<!italic>".length());
		}
		return serialize;
	}
	
	public static String serializeLegacy(Component str) {
		return LegacyComponentSerializer.legacyAmpersand().serializeOrNull(str);
	}
	
	/**
	 * show title
	 *
	 * @param uuid     player uuid
	 * @param title    title string
	 * @param subtitle subtitle string
	 */
	
	public static void title(UUID uuid, Object title, Object subtitle) {
		title(Bukkit.getPlayer(uuid), title, subtitle);
	}
	
	/**
	 * show title
	 *
	 * @param player   player
	 * @param title    title string
	 * @param subtitle subtitle string
	 */
	
	public static void title(Player player, Object title, Object subtitle) {
		if (player != null && player.isOnline()) {
			player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
				Util.mm(Objects.requireNonNullElse(subtitle, ""))));
		}
	}
	
	public static void title(Player player, Object title, Object subtitle, int seconds) {
		if (player != null && player.isOnline()) {
			player.showTitle(Title.title(Util.mm(Objects.requireNonNullElse(title, "")),
				Util.mm(Objects.requireNonNullElse(subtitle, "")), Title.Times.times(Duration.ZERO, Duration.ofSeconds(seconds), Duration.ZERO)));
		}
	}
	
	/**
	 * BroadCast
	 *
	 * @param str message
	 */
	
	public static void Cast(Object str) {
		Bukkit.broadcast(Util.mm(str));
	}
	
}
