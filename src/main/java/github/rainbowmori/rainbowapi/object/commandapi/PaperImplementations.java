package github.rainbowmori.rainbowapi.object.commandapi;

import github.rainbowmori.rainbowapi.object.commandapi.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

public class PaperImplementations {

	private final boolean isPaperPresent;
	private final NMS<?> nmsInstance;

	/**
	 * Constructs a PaperImplementations object
	 * 
	 * @param isPaperPresent Whether this is a Paper server or not
	 * @param nmsInstance    The instance of NMS
	 */
	public PaperImplementations(boolean isPaperPresent, NMS<?> nmsInstance) {
		this.isPaperPresent = isPaperPresent;
		this.nmsInstance = nmsInstance;
	}


	/**
	 * @return Bukkit's {@link CommandMap}
	 */
	public CommandMap getCommandMap() {
		if (isPaperPresent) {
			return Bukkit.getServer().getCommandMap();
		} else {
			return nmsInstance.getSimpleCommandMap();
		}
	}
	
	/**
	 * @return whether we're using paper or not
	 */
	public boolean isPresent() {
		return this.isPaperPresent;
	}

}
