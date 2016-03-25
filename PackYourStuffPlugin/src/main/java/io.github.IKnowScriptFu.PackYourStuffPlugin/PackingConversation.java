package io.github.IKnowScriptFu.PackYourStuffPlugin;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Aemilius on 09/03/2016.
 */
public class PackingConversation extends ConversationFactory {
    public PackingConversation(Plugin plugin, Function<Player, Void> callback) {
        super(plugin);
        this.firstPrompt = new confirmationPrompt();
        this.callback = callback;
    }

    private Function<Player, Void> callback;

    private class confirmationPrompt extends BooleanPrompt{

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, boolean b) {
            if(b){
                callback.apply((Player) conversationContext.getForWhom());
            }
            return null;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext){
            conversationContext.getForWhom().sendRawMessage("Face the front face of your structure. Then type: " );
            return ChatColor.GREEN + "y" + ChatColor.WHITE + " or " + ChatColor.RED + "n";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n");
        }

    }
}
