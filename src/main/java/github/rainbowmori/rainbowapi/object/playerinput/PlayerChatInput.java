package github.rainbowmori.rainbowapi.object.playerinput;

import github.rainbowmori.rainbowapi.RainbowAPI;
import github.rainbowmori.rainbowapi.util.Util;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayerChatInput implements ConversationAbandonedListener {

    private final Plugin plugin;

    private final ConversationFactory factory;

    public PlayerChatInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt) {
        var factory = new ConversationFactory(plugin).withPrefix(context -> prefix).withFirstPrompt(prompt).
                withLocalEcho(false);
        if (cancelable) factory.withEscapeSequence("cancel");
        factory.addConversationAbandonedListener(this).thatExcludesNonPlayersWithMessage("プレイヤーしか入力できません");
        this.factory = factory;
        this.plugin = plugin;
    }

    public PlayerChatInput(Plugin plugin, boolean cancelable, Prompt prompt) {
        this(plugin, RainbowAPI.apis.get(plugin).prefix, cancelable, prompt);
    }

    public PlayerChatInput(Plugin plugin, boolean cancelable, Prompt prompt, int timeout) {
        this(plugin, RainbowAPI.apis.get(plugin).prefix, cancelable, prompt, timeout);
    }

    public PlayerChatInput(Plugin plugin, String prefix, boolean cancelable, Prompt prompt, int timeout) {
        this(plugin, prefix, cancelable, prompt);
        this.factory.withTimeout(timeout);
    }

    public static boolean isInputted(Player player) {
        if (player.isConversing()) {
            Util.sendRM(player, "<red>現在入力中です");
            return true;
        }
        return false;
    }

    public void build(Player player) {
        if (isInputted(player))
            throw new RuntimeException("入力中なのにまた入力のメゾットを使用しています player = " + player.getName());
        else factory.buildConversation(player).begin();
    }

    @Override
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent abandonedEvent) {
        if (!abandonedEvent.gracefulExit()) abandonedEvent.getContext().getForWhom().sendRawMessage("入力をキャンセルしました");
    }
}
