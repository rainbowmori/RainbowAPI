package github.rainbowmori.rainbowapi.object.command.exceptions;


public class InvalidRangeException extends RuntimeException {


	public InvalidRangeException() {
		super("最小値より小さい最大値を持つことはできません");
	}
	
}
