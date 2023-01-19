package github.rainbowmori.rainbowapi.object.ui.action;

public abstract class MenuCommandAction implements MenuAction {

    private final String command;

    public MenuCommandAction(String command) {
        this.command = command;
    }

    public final String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return null;
    }
}
