package com.bot.listeners;

import com.bot.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class EventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String message = event.getMessage().getContentRaw().toLowerCase().replaceAll("\\s", "");
            HashMap<String, String> answers = (HashMap<String, String>) Config.getInstance().getConfig().get("answers");
            if (answers.containsKey(message)) {
                event.getChannel().sendMessage(answers.get(message)).queue();
            }
        }
    }
}
