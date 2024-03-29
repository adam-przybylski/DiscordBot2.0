package com.bot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Utils {

    public static SelfUser selfUser;

    public static void joinVoiceChannel(SlashCommandInteractionEvent event) {
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("You need to be in the voice channel for this command to work").queue();
            return;
        }

        if (!event.getGuild().getAudioManager().isConnected()) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }
    }

    public static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static String formatTrackInfo(AudioTrack track) {
        return "**"
                .concat(track.getInfo().title)
                .concat("** by **")
                .concat(track.getInfo().author)
                .concat("**");
    }
}
