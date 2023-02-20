package github.rainbowmori.rainbowapi.object.commandapi.arguments;

import github.rainbowmori.rainbowapi.object.commandapi.arguments.EntitySelectorArgument.ManyEntities;
import github.rainbowmori.rainbowapi.object.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import github.rainbowmori.rainbowapi.object.commandapi.arguments.EntitySelectorArgument.OneEntity;
import github.rainbowmori.rainbowapi.object.commandapi.arguments.EntitySelectorArgument.OnePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * An enum that represents single entities or players, as well as collections of
 * entities or players
 * 
 * @deprecated Use {@code EntitySelectorArgument.}{@link OneEntity},
 *             {@code EntitySelectorArgument.}{@link OnePlayer},
 *             {@code EntitySelectorArgument.}{@link ManyEntities} or
 *             {@code EntitySelectorArgument.}{@link ManyPlayers}
 */
@Deprecated(forRemoval = true, since = "8.7.0")
public enum EntitySelector {
	/**
	 * A single entity. Returns a {@link Entity}
	 */
	ONE_ENTITY,
	
	/**
	 * A single player. Returns a {@link Player}
	 */
	ONE_PLAYER,
	
	/**
	 * Many entities. Returns a {@link Collection}{@code <}{@link Entity}{@code >}
	 */
	MANY_ENTITIES,
	
	/**
	 * Many players. Returns a {@link Collection}{@code <}{@link Player}{@code >}
	 */
	MANY_PLAYERS;
}