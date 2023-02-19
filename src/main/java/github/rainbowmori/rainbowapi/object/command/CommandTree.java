package github.rainbowmori.rainbowapi.object.command;

import java.util.ArrayList;
import java.util.List;

public class CommandTree extends ExecutableCommand<CommandTree> {

	private final List<ArgumentTree> arguments = new ArrayList<>();

	public CommandTree(final String commandName) {
		super(commandName);
	}

	public CommandTree then(final ArgumentTree tree) {
		this.arguments.add(tree);
		return this;
	}

	public void register() {
		List<Execution> executions = new ArrayList<>();
		if (this.executor.hasAnyExecutors()) {
			executions.add(new Execution(new ArrayList<>(), this.executor));
		}
		for (ArgumentTree tree : arguments) {
			executions.addAll(tree.getExecutions());
		}
		for (Execution execution : executions) {
			execution.register(this.meta);
		}
	}

}
