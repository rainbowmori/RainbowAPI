package github.rainbowmori.rainbowapi.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

public class McUtil {
    private final String prefix;
    private final Logger log;
    
    public McUtil(String prefix,String logName) {
        this(prefix, Logger.getLogger(logName));
    }

    public McUtil(String prefix, Logger log) {
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
            sender.sendMessage(Util.mm(prefix + str));
        }
    }
    
    public void Cast(Object str) {
        Bukkit.broadcast(Util.mm(prefix + str));
    }
    
    public void consoleCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void executeCommand(Player execute, String command) {
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
}
