package github.rainbowmori.rainbowapi.object.playerinput;

import org.bukkit.conversations.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayerInput implements ConversationAbandonedListener {

    private final ConversationFactory factory;
    public PlayerInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt) {
        var factory = new ConversationFactory(plugin).withPrefix(context -> prefix).withFirstPrompt(prompt);
        if(cancelable) factory.withEscapeSequence("cancel");
        factory.addConversationAbandonedListener(this).thatExcludesNonPlayersWithMessage("プレイヤーしか入力できません");
        this.factory = factory;
    }

    public PlayerInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt,int timeout) {
        this(plugin, prefix, cancelable, prompt);
        this.factory.withTimeout(timeout);
    }

    @Override
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent abandonedEvent) {
        if(!abandonedEvent.gracefulExit()) abandonedEvent.getContext().getForWhom().sendRawMessage("入力をキャンセルしました");
    }
}
