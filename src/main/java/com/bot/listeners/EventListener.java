package com.bot.listeners;

import com.bot.Config;
import com.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
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

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        HashMap<String, String> map = (HashMap<String, String>) Config.getInstance().getConfig().get("reactions_to_user_join_vc");
        if (map.containsKey(member.getId()) && member.getVoiceState().inAudioChannel() && event.getChannelLeft() == null) {
            PlayerManager.getInstance().playLocalTrack(event.getGuild(), "tracks/" + map.get(member.getId()) + ".mp3");
        }
    }
}
