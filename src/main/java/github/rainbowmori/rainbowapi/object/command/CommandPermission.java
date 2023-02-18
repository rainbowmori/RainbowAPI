package github.rainbowmori.rainbowapi.object.command;

import java.util.Optional;

public class CommandPermission {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (negated ? 1231 : 1237);
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((permissionNode == null) ? 0 : permissionNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandPermission other = (CommandPermission) obj;
		if (negated != other.negated)
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		return permissionNode == other.permissionNode;
	}

	public static CommandPermission OP = new CommandPermission(PermissionNode.OP);

	public static CommandPermission NONE = new CommandPermission(PermissionNode.NONE);

	public static CommandPermission fromString(String permission) {
		return new CommandPermission(permission);
	}

	private final String permission;
	private final PermissionNode permissionNode;
	private boolean negated = false;

	private CommandPermission(String permission) {
		this.permission = permission;
		this.permissionNode = null;
	}

	private CommandPermission(PermissionNode permissionNode) {
		this.permission = null;
		this.permissionNode = permissionNode;
	}
	
	Optional<String> getPermission() {
		return Optional.ofNullable(this.permission);
	}

	boolean isNegated() {
		return this.negated;
	}
	
	CommandPermission negate() {
		this.negated = true;
		return this;
	}

	@Override
	public String toString() {
		final String result;
		if(permissionNode != null) {
			if(permissionNode == PermissionNode.OP) {
				result = "OP";
			} else {
				result =  "NONE";
			}
		} else {
			result = permission;
		}
		return (negated ? "not " : "") + result;
	}
	
	private enum PermissionNode {
		OP, 

		NONE
	}
	
}
