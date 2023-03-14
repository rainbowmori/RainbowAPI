package github.rainbowmori.rainbowapi.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.logging.Logger;

public class PrefixUtil {
	
	private final String prefix;
	private final Logger log;
	
	public PrefixUtil(String prefix, String logName) {
		this(prefix, Logger.getLogger(logName));
	}
	
	public PrefixUtil(String prefix, Logger log) {
		this.prefix = prefix;
		this.log = log;
	}
	
	public void logInfo(String message) {
		log.info(message);
	}
	
	public void logNormal(String message) {
		log.info(message);
	}
	
	public void logWarning(String message) {
		log.warning(message);
	}
	
	public void logError(String message) {
		log.severe(message);
	}
	
	public void send(UUID uuid, Object str) {
		send(Bukkit.getPlayer(uuid), str);
	}
	
	public void send(CommandSender sender, Object str) {
		if (sender != null) {
			sender.sendMessage(Util.mm(prefix + (IsObjectUtil.IsComponent(str) ? Util.serialize((Component) str) :
				str.toString())));
		}
	}
	
	public void Cast(String str) {
		Bukkit.broadcast(Util.mm(prefix + str));
	}
	
}
