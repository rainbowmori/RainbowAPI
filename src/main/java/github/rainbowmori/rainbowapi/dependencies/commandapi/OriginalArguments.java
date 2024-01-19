package github.rainbowmori.rainbowapi.dependencies.commandapi;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.StringArgument;

public class OriginalArguments {

  public static final String[] material = Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new);

  public static Argument<Material> materials(String nodeName) {
    return new CustomArgument<>(new StringArgument(nodeName), info -> {
      Material material = Material.getMaterial(info.input());
      if (material == null) {
        throw CustomArgumentException.fromMessageBuilder(
            new MessageBuilder("Unknown material : ").appendArgInput());
      }
      return material;
    }).replaceSuggestions(ArgumentSuggestions.strings(material));
  }

  public static Argument<String> playerNames(String nodeName) {
    return new StringArgument(nodeName).replaceSuggestions(
        ArgumentSuggestions.strings(
            Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()));
  }

  public static Argument<Player> onlinePlayer(String nodeName) {
    return new CustomArgument<>(
        playerNames(nodeName),
        info -> {
          Player player = Bukkit.getPlayer(info.currentInput());
          if (player == null) {
            throw CustomArgumentException.fromMessageBuilder(
                new MessageBuilder("Unknown player : ")
                    .appendArgInput());
          }
          return player;
        })
        .replaceSuggestions(ArgumentSuggestions.strings(
            Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()));
  }

  public static Argument<World> worlds(String nodeName) {
    return new CustomArgument<>(
        new StringArgument(nodeName),
        info -> {
          World world = Bukkit.getWorld(info.input());
          if (world == null) {
            throw CustomArgumentException.fromMessageBuilder(
                new MessageBuilder("Unknown world : ").appendArgInput());
          }
          return world;
        })
        .replaceSuggestions(ArgumentSuggestions.strings(
            Bukkit.getWorlds().stream().map(World::getName).toList()));
  }
}
