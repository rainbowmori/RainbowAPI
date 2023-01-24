package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.RainbowAPI;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayerInput implements ConversationAbandonedListener {

    private final Plugin plugin;

    private final ConversationFactory factory;
    public PlayerInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt) {
        var factory = new ConversationFactory(plugin).withPrefix(context -> prefix + " ").withFirstPrompt(prompt).
                withLocalEcho(false);
        if(cancelable) factory.withEscapeSequence("cancel");
        factory.addConversationAbandonedListener(this).thatExcludesNonPlayersWithMessage("プレイヤーしか入力できません");
        this.factory = factory;
        this.plugin = plugin;
    }

    public PlayerInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt,int timeout) {
        this(plugin, prefix, cancelable, prompt);
        this.factory.withTimeout(timeout);
    }

    public void build(Player player) {
        if(isInputted(plugin,player)) throw new RuntimeException("入力中なのにまた入力のメゾットを使用しています player = " + player.getName());
        else factory.buildConversation(player).begin();
    }

    @Override
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent abandonedEvent) {
        if(!abandonedEvent.gracefulExit()) abandonedEvent.getContext().getForWhom().sendRawMessage("入力をキャンセルしました");
    }

    public static boolean isInputted(Plugin plugin,Player player) {
        if (player.isConversing()) {
            RainbowAPI.apis.get(plugin).mcUtil.send(player, "<red>現在入力中です");
            return true;
        }
        return false;
    }
}
