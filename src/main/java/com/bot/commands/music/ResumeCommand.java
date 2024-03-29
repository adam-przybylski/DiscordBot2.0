package com.bot.commands.music;

import com.bot.commands.Command;
import com.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class ResumeCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getHook().sendMessage("You must be in a voice channel to use this command").queue();
            return;
        }
        if (player.getPlayingTrack() == null) {
            event.getHook().sendMessage("No song is currently playing").queue();
            return;
        }
        if (!player.isPaused()) {
            event.getHook().sendMessage("The player is not paused").queue();
            return;
        }
        player.setPaused(false);
        event.getHook().sendMessage("Resumed").queue();
    }

    @Override
    public String getDescription() {
        return "Resume the current song";
    }

    @Override
    public String getName() {
        return "resume";
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
