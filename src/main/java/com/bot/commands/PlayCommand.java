package com.bot.commands;

import com.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getChannel().sendMessage("You need to be in the voice channel for this command to work").queue();
            return;
        }

        if (!event.getGuild().getAudioManager().isConnected()) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        System.out.println(event.getOption("url").getAsString());
        String link = String.join(" ", event.getOption("url").getAsString());
        System.out.println(link);        if (!isUrl(link)) {
            link = "ytsearch: " + link + " audio";
        }
        System.out.println("2");
        PlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(), link);
        System.out.println("3");
        event.getHook().sendMessage("xd").queue();
    }

    public boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Play a song";
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "url", "Link or name of the song", true)
        );
    }
}
