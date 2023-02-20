package github.rainbowmori.rainbowapi.object.commandapi.exceptions;

public class UnknownArgumentException extends Exception {

	/**
	 * An exception caused when an unknown argument type is provided
	 * @param argument the argument that wasn't recognised
	 */
	public UnknownArgumentException(String argument) {
		super("The argument type '" + argument + "' is not recognized!");
	}

}
