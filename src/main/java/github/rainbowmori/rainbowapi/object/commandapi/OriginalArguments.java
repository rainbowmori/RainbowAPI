package github.rainbowmori.rainbowapi.object.commandapi;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class OriginalArguments {
	
	public static String[] material = Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new);
	
	public static Argument<Material> materials(String nodeName) {
		return new CustomArgument<>(new StringArgument(nodeName), info -> {
			Material material = Material.getMaterial(info.input());
			if (material == null) {
				throw new CustomArgument.CustomArgumentException(
					new CustomArgument.MessageBuilder("Unknown material : ").appendArgInput());
			}
			return material;
		}).replaceSuggestions(ArgumentSuggestions.strings(material));
	}
	
	public static Argument<String> playerNames(String nodeName) {
		return new StringArgument(nodeName).replaceSuggestions(ArgumentSuggestions.strings(
			Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()));
	}
	
	public static Argument<Player> onlinePlayer(String nodeName) {
		return new CustomArgument<>(playerNames(nodeName), info -> {
			Player player = Bukkit.getPlayer(info.currentInput());
			if (player == null) {
				throw new CustomArgument.CustomArgumentException(
					new CustomArgument.MessageBuilder("Unknown player : ").appendArgInput());
			}
			return player;
		});
	}
	
	public static Argument<World> worlds(String nodeName) {
		return new CustomArgument<>(new StringArgument(nodeName), info -> {
			World world = Bukkit.getWorld(info.input());
			if (world == null) {
				throw new CustomArgument.CustomArgumentException(
					new CustomArgument.MessageBuilder("Unknown world : ").appendArgInput());
			}
			return world;
		}).replaceSuggestions(ArgumentSuggestions.strings(Bukkit.getWorlds().stream().map(World::getName).toList()));
	}
}