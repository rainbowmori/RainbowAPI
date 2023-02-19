
package github.rainbowmori.rainbowapi.object.command.exceptions;


public class InvalidCommandNameException extends RuntimeException {
	

	public InvalidCommandNameException(String commandName) {
		super("無効なコマンド名 [" + commandName + "] は登録できません!");
	}
	
}
