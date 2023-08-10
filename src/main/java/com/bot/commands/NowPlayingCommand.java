package com.bot.commands;

import com.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class NowPlayingCommand implements Command {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        AudioTrack currentTrack = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
        if (currentTrack == null) {
            event.getHook().sendMessage("No song is currently playing").queue();
            return;
        }
        String message = "**"
                .concat(currentTrack.getInfo().title)
                .concat("** by **")
                .concat(currentTrack.getInfo().author);
        event.getHook().sendMessage("Currently playing: " + message).queue();
    }

    @Override
    public String getDescription() {
        return "Name of the currently playing song";
    }

    @Override
    public String getName() {
        return "now_playing";
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
