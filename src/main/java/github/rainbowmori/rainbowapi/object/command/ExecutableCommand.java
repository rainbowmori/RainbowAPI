package github.rainbowmori.rainbowapi.object.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.function.Predicate;

abstract class ExecutableCommand<T extends ExecutableCommand<T>> extends Executable<T> {

	protected final CommandMetaData meta;

	ExecutableCommand(final String commandName) {
		this.meta = new CommandMetaData(commandName);
	}

	protected ExecutableCommand(final CommandMetaData meta) {
		this.meta = meta;
	}

	public String getName() {
		return meta.commandName;
	}

	public T withPermission(CommandPermission permission) {
		this.meta.permission = permission;
		return (T) this;
	}

	public T withPermission(String permission) {
		this.meta.permission = CommandPermission.fromString(permission);
		return (T) this;
	}

	public T withoutPermission(CommandPermission permission) {
		this.meta.permission = permission.negate();
		return (T) this;
	}

	public T withoutPermission(String permission) {
		this.meta.permission = CommandPermission.fromString(permission).negate();
		return (T) this;
	}

	public T withRequirement(Predicate<CommandSender> requirement) {
		this.meta.requirements = this.meta.requirements.and(requirement);
		return (T) this;
	}

	public T withAliases(String... aliases) {
		this.meta.aliases = aliases;
		return (T) this;
	}

	public CommandPermission getPermission() {
		return this.meta.permission;
	}

	public void setPermission(CommandPermission permission) {
		this.meta.permission = permission;
	}

	public String[] getAliases() {
		return meta.aliases;
	}

	public void setAliases(String[] aliases) {
		this.meta.aliases = aliases;
	}

	public Predicate<CommandSender> getRequirements() {
		return this.meta.requirements;
	}

	public void setRequirements(Predicate<CommandSender> requirements) {
		this.meta.requirements = requirements;
	}

	public String getShortDescription() {
		return this.meta.shortDescription.orElse(null);
	}

	public T withShortDescription(String description) {
		this.meta.shortDescription = Optional.ofNullable(description);
		return (T) this;
	}

	public String getFullDescription() {
		return this.meta.fullDescription.orElse(null);
	}

	public T withFullDescription(String description) {
		this.meta.fullDescription = Optional.ofNullable(description);
		return (T) this;
	}

	public T withHelp(String shortDescription, String fullDescription) {
		this.meta.shortDescription = Optional.ofNullable(shortDescription);
		this.meta.fullDescription = Optional.ofNullable(fullDescription);
		return (T) this;
	}


	public void override(Plugin plugin) {
		Bukkit.getServer().getCommandMap().getKnownCommands().remove(this.meta.commandName);
		register(plugin);
	}

	public abstract void register(Plugin plugin);
	
}
