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

/**
 * Created by Aemilius on 09/03/2016.
 */
public class PackingConversation extends ConversationFactory {
    public PackingConversation(Plugin plugin, Function<Player, Void> callback) {
        super(plugin);
        this.firstPrompt = new confirmationPrompt();
        this.callback = callback;
    }

    Function<Player, Void> callback;

    class confirmationPrompt extends BooleanPrompt{

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, boolean b) {
            return null;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext){
            conversationContext.getForWhom().sendRawMessage("Are you sure you want to do " +
                "that? Use " + ChatColor.GREEN + "/yes " + ChatColor.WHITE + "to continue or " + ChatColor.RED + "/no "
                    + ChatColor.WHITE + "to cancel");
            return null;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if(input.equalsIgnoreCase("/yes")){
                callback.apply((Player) context.getForWhom());
            }
            return null;
        }
    }
}
