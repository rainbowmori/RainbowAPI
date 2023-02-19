
package github.rainbowmori.rainbowapi.object.command.exceptions;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.io.PrintStream;
import java.io.PrintWriter;

public class WrapperCommandSyntaxException extends Exception {

	private final CommandSyntaxException exception;

	public WrapperCommandSyntaxException(CommandSyntaxException exception) {
		this.exception = exception;
	}

	public CommandSyntaxException getException() {
		return this.exception;
	}

	@Override
	public String getMessage() {
		return this.exception.getMessage();
	}

	@Override
	public synchronized Throwable getCause() {
		return this.exception.getCause();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return this.exception.getStackTrace();
	}

	@Override
	public synchronized Throwable initCause(Throwable cause) {
		return this.exception.initCause(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return this.exception.getLocalizedMessage();
	}

	@Override
	public void printStackTrace() {
		this.exception.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		this.exception.printStackTrace(s);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		this.exception.printStackTrace(s);
	}

	@Override
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.exception.setStackTrace(stackTrace);
	}

	public Message getRawMessage() {
		return this.exception.getRawMessage();
	}

	public String getContext() {
		return this.exception.getContext();
	}
	public CommandExceptionType getType() {
		return this.exception.getType();
	}

	public String getInput() {
		return this.exception.getInput();
	}

	public int getCursor() {
		return this.exception.getCursor();
	}

}
