
package github.rainbowmori.rainbowapi.object.command.exceptions;


public class BadLiteralException extends RuntimeException {

	public BadLiteralException(boolean isNull) {
		super(isNull ? "null 文字列で LiteralArgument を作成することはできません" : "空の文字列で LiteralArgument を作成することはできません");
	}
	
}
