package com.bot.commands;

import com.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class PauseCommand implements Command{
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        if(!event.getMember().getVoiceState().inAudioChannel()) {
            event.getHook().sendMessage("You must be in a voice channel to use this command").queue();
            return;
        }
        if (player.getPlayingTrack() == null) {
            event.getHook().sendMessage("No song is currently playing").queue();
            return;
        }
        if (player.isPaused()) {
            event.getHook().sendMessage("The player is already paused").queue();
            return;
        }
        player.setPaused(true);
        event.getHook().sendMessage("Paused").queue();
    }

    @Override
    public String getDescription() {
        return "Pause the current song";
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(getOptions());
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
