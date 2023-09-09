package github.rainbowmori.rainbowapi.commnad;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;

public class RainbowAPICommand extends CommandAPICommand {

  public RainbowAPICommand() {
    super("rainbowapi");
    withPermission(CommandPermission.OP);
    withSubcommand(new CustomItemsCommand());
  }
}
