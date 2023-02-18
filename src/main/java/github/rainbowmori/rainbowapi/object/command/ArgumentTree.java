package github.rainbowmori.rainbowapi.object.command;

import github.rainbowmori.rainbowapi.object.command.arguments.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentTree extends Executable<ArgumentTree> {

	final List<ArgumentTree> arguments = new ArrayList<>();
	final Argument<?> argument;
	protected ArgumentTree() {
		if (!(this instanceof Argument<?> argument)) {
			throw new IllegalArgumentException("Implicit inherited constructor must be from Argument");
		}
		this.argument = argument;
	}

	public ArgumentTree(final Argument<?> argument) {
		this.argument = argument;
		this.executor = argument.executor;
	}

	public ArgumentTree then(final ArgumentTree tree) {
		this.arguments.add(tree);
		return this;
	}

	List<Execution> getExecutions() {
		List<Execution> executions = new ArrayList<>();
		if (this.executor.hasAnyExecutors()) {
			executions.add(new Execution(List.of(this.argument), this.executor));
		}
		for (ArgumentTree tree : arguments) {
			for (Execution execution : tree.getExecutions()) {
				executions.add(execution.prependedBy(this.argument));
			}
		}
		return executions;
	}

}
